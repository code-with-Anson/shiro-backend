package com.shiro.backend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiro.backend.domain.po.Users;
import com.shiro.backend.mapper.UsersMapper;
import com.shiro.backend.service.IUsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.shiro.backend.enums.Gender.MALE;
import static com.shiro.backend.enums.isDeletedEnum.isDeleted;
import static com.shiro.backend.enums.isDeletedEnum.notDeleted;

@SpringBootTest
class UsersTest {
    @Autowired
    private IUsersService usersService;
    @Autowired
    private UsersMapper usersMapper;

    //增加新用户
    @Test
    void insertUser() {
        Users user = new Users();
        user.setName("admin");
        user.setPassword("123456");
        user.setSex(MALE);
        user.setIsDeleted(notDeleted);
        usersService.save(user);
    }

    //批量增加用户
    @Test
    void batchInsertUser() {
        List<Users> usersList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Users user = new Users();
            user.setName("user" + i);
            user.setPassword("123456");
            user.setSex(MALE);
            user.setIsDeleted(notDeleted);
            usersList.add(user);
        }
        usersService.saveBatch(usersList);
    }

    //通过id查找用户
    @Test
    void findUser() {
        System.out.println(usersService.getById(1876330044003987457L));
    }

    //通过LambdaQueryWrapper查询
    @Test
    void findMaleUsers() {
        //1.构造查询条件
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<Users>()
                .select(Users::getId, Users::getName)
                .eq(Users::getSex, MALE);
        //2.批量查询男性用户
        List<Users> usersList = usersMapper.selectList(queryWrapper);
        usersList.forEach(System.out::println);
    }

    //删除指定用户
    @Test
    void deleteUser() {
        usersService.removeById(1876529961481883650L);
        System.out.println("目前查找删除后的用户是：" + usersService.getById(1876529961481883650L));
    }

    //查找所有被逻辑删除的用户
    @Test
    void findDeletedUsers() {
        List<Users> deletedUsers = usersMapper.findDeletedUsers();
        System.out.println("下面罗列出被逻辑删除的用户：");
        deletedUsers.forEach(System.out::println);
    }

    //根据逻辑删除状态查找用户
    @Test
    void findUsersByDeletedStatus() {
        List<Users> users = usersMapper.findUsersByDeletedStatus(isDeleted);
        System.out.println("下面列出逻辑删除状态为" + users.get(0).getIsDeleted() + "的学生:");
        users.forEach(System.out::println);
    }

    @Test
    void findUsersByEmail() {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<Users>()
                .eq(Users::getEmail, "123@123.com");

        List<Users> usersList = usersMapper.selectList(queryWrapper);
        usersList.forEach(System.out::println);
    }
}
