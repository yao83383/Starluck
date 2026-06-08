package com.starluck.config;

import com.starluck.entity.User;
import com.starluck.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        User admin = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getPhone, "13800000000")
                        .eq(User::getRole, "ADMIN"));
        if (admin == null) {
            User user = new User();
            user.setPhone("13800000000");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setRole("ADMIN");
            user.setStatus(1);
            userMapper.insert(user);
            log.info("默认管理员账号已创建: 13800000000 / admin123");
        } else {
            boolean valid = admin.getPassword() != null && admin.getPassword().startsWith("$2a$");
            if (!valid) {
                admin.setPassword(passwordEncoder.encode("admin123"));
                userMapper.updateById(admin);
                log.info("管理员密码已修正");
            }
        }
    }
}
