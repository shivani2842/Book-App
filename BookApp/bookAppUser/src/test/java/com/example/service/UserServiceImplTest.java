
package com.example.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import com.example.exception.BookAlreadyExistsException;

import com.example.model.User;

import com.example.repo.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
	
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private UserServiceImpl service;
	User user;
	List<User> listuser;
	Optional<User> optUser;
	@Before
	public void setUp() throws Exception {
		user = new User("shivanipatel@gmail.com","shivani","female","bhopal","shivani","image1");
		optUser = Optional.of(user);
	}
	@Test
	public void testAddUserSuccess() throws BookAlreadyExistsException {
		// BookRepository repo = Mockito.mock(BookRepository.class);
//		when(favRepository.findByBookIdAndUsername(Mockito.anyString(),Mockito.anyString())).
//					thenReturn(listfav);
		when(userRepository.save(Mockito.any(User.class))).
					thenReturn(user);
		User addedBook = service.registeruser(user);
//		assertEquals(fav.getBookId(), addedBook.getBookId());
//		verify(favRepository).findByBookIdAndUsername(Mockito.anyString(),Mockito.anyString());
		verify(userRepository).save(Mockito.any());
	}


}
