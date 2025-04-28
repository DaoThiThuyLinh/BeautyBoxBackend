package org.beautybox.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.beautybox.entity.OrderItem;
import org.beautybox.entity.OrderProduct;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.mapper.UserMapper;
import org.beautybox.repository.RoleRepository;
import org.beautybox.repository.UserRepository;
import org.beautybox.request.UserRegisterRequest;
import org.beautybox.response.PageResponse;
import org.beautybox.response.UserDetailResponse;
import org.beautybox.response.UserResponse;
import org.beautybox.service.JwtService;
import org.beautybox.service.UserService;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService, UserDetailsService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    JwtService jwtService;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    @PersistenceContext
    EntityManager entityManager;

    @SneakyThrows
    @Override
    public void register(UserRegisterRequest registerRequest) {
        if(!registerRequest.getPassword().equals(registerRequest.getPasswordConfirm())){
            throw new BeautyBoxException(ErrorDetail.ERR_PASSWORD_CONFIRM_INCORRECT);
        }
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new BeautyBoxException(ErrorDetail.ERR_USER_EMAIL_EXISTED);
        }
        User user =  userMapper.fromRegisterRequest(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserResponse getUserByToken(String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findUserByEmail(username);
        return userMapper.toResponse(user);
    }

    @Override
    public PageResponse<UserDetailResponse> getAllUser(String name, int pageIndex, int pageSize, String orderBy, String direction) {
        String properties = switch (orderBy) {
            case "2" -> "totalOrder";
            case "3" -> "name";
            case "4" -> "totalRevenue";
            default -> "createdAt";
        };

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Object[]> usersPage = this.findUsersByName(name, pageable, properties, direction);
        List<UserDetailResponse> userDetailResponses = new ArrayList<>();
        for(Object[] data : usersPage.getContent()){
            UserDetailResponse response = userMapper.toDetailResponse((User) data[0]);
            response.setTotalOrder((long) data[1]);
            response.setTotalRevenue((long) data[2]);
            userDetailResponses.add(response);
        }
        return PageResponse.<UserDetailResponse>builder()
                .totalPages(usersPage.getTotalPages())
                .totalElements(usersPage.getTotalElements())
                .content(userDetailResponses)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .sortBy(new PageResponse.SortBy(properties, direction))
                .build();
    }


    public Page<Object[]> findUsersByName(String name, Pageable pageable, String property, String direction) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<User> user = query.from(User.class);

        Join<User, Order> order = user.join("orders", JoinType.LEFT);
        Join<Order, OrderItem> orderItem = order.join("orderItems", JoinType.LEFT);

        Expression<Long> countOrders = cb.countDistinct(order);
        Expression<?> sumRevenue = cb.coalesce(
                cb.sum(
                        cb.diff(
                                orderItem.get("price"),
                                cb.prod(
                                        orderItem.get("price"),
                                        cb.quot(orderItem.get("discount"), 100)
                                )
                        )
                ),
                0.0
        );
        query.multiselect(
                user,
                countOrders.alias("totalOrder"),
                sumRevenue.alias("totalRevenue")
        );
        if (name != null && !name.isEmpty()) {
            query.where(cb.equal(user.get("name"), name));
        }
        query.groupBy(user);
        if (property != null && !property.isEmpty()) {
            if ("totalOrder".equals(property)) {
                if(direction.equalsIgnoreCase("desc")) {
                    query.orderBy(cb.desc(countOrders));
                }else {
                    query.orderBy(cb.asc(countOrders));
                }
            } else if ("totalRevenue".equals(property)) {
                if(direction.equalsIgnoreCase("desc")) {
                    query.orderBy(cb.desc(sumRevenue));
                }else {
                    query.orderBy(cb.asc(sumRevenue));
                }
            } else {
                if(direction.equalsIgnoreCase("desc")) {
                    query.orderBy(cb.desc(user.get(property)));
                }else {
                    query.orderBy(cb.asc(user.get(property)));
                }
            }
        }

        // Phân trang
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        // Đếm tổng số lượng
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countUser = countQuery.from(User.class);
        countQuery.select(cb.count(countUser));
        if (name != null && !name.isEmpty()) {
            countQuery.where(cb.equal(countUser.get("name"), name));
        }
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(typedQuery.getResultList(), pageable, total);
    }
}
