package com.example.link.reduce;

import com.example.link.reduce.data.entity.UserEntity;
import com.example.link.reduce.service.UserService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApplicationTests {

	private UserService service;

	@Autowired
	public ApplicationTests(UserService service) {
		this.service = service;
	}

//	@Test
//    public void saveDataTest() {
//		val name = "lid";
//        val user = new UserEntity();
//		user.setId(0l);
//		user.setName(name);
//		user.setPassword("pass");
//        service.saveUser(user);
//
//        System.out.println(user);
//
//        assertTrue(service.getUserByLogin(name).isPresent());
////                assertEquals(1, 1);
//
//    }

}
