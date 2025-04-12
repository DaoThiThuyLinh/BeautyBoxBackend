package org.beautybox;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.beautybox.entity.Role;
import org.beautybox.entity.User;
import org.beautybox.mapper.UserMapper;
import org.beautybox.repository.RoleRepository;
import org.beautybox.repository.UserRepository;
import org.beautybox.request.UserRegisterRequest;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication(exclude = {ElasticsearchRestClientAutoConfiguration.class})
@RequiredArgsConstructor
public class BeautyBoxApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(BeautyBoxApplication.class, args);
    }

    final RoleRepository roleRepository;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final EntityManager entityManager;
    final UserMapper userMapper;

    @SneakyThrows
    @Transactional
    @Override
    public void run(String... args){
        if(!roleRepository.existsByName("ROLE_ADMIN")) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
        if(!roleRepository.existsByName("ROLE_USER")) {
            roleRepository.save(new Role("ROLE_USER"));
        }
        if(!userRepository.existsByEmail("beautybox@gmail.com")) {
            UserRegisterRequest registerRequest = new UserRegisterRequest();
            registerRequest.setEmail("beautybox@gmail.com");
            registerRequest.setName("BeautyBox");
            String password = passwordEncoder.encode("Admin@1234");
            registerRequest.setPassword(password);
            registerRequest.setGender("Undefined");
            registerRequest.setPhone("Undefined");
            User user = userMapper.fromRegisterRequest(registerRequest);
            user.setRole(roleRepository.findByName("ROLE_ADMIN"));

            userRepository.save(user);
        }else{
            User user = userRepository.findUserByEmail("beautybox@gmail.com");
            user.setRole(roleRepository.findByName("ROLE_ADMIN"));
            userRepository.save(user);
        }
        Search.session(entityManager)
                .massIndexer()
                .startAndWait();
    }
}
