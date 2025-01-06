package com.shiro.backend;

import com.shiro.backend.domain.po.Users;
import com.shiro.backend.service.IUsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.shiro.backend.enums.Gender.MALE;
import static com.shiro.backend.enums.isDeletedEnum.notDeleted;

@SpringBootTest
class ShiroBackendApplicationTests {
    @Autowired
    private IUsersService usersService;

    @Test
    void contextLoads() {
    }

    @Test
    void insertUser() {
        Users user = new Users();
        user.setName("admin");
        user.setPassword("123456");
        user.setSex(MALE);
        user.setIsDeleted(notDeleted);
        usersService.save(user);
    }

    @Test
    void findUser() {
        System.out.println(usersService.getById(1876330044003987457L));
    }

    @Test
    void deleteUser() {
        usersService.removeById(1876330044003987457L);
        System.out.println("目前查找删除后的用户是：" + usersService.getById(1876330044003987457L));
    }
}
