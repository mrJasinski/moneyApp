package com.moneyApp.user.service;

class UserServiceUnitTest
{
//    @Test
//    void validateEmail_shouldReturnTrueForValidEmailAddress()
//    {
////        given
//        var mail1 = "example@example.com";
//        var mail2 = "mr.example@example.com";
//        var mail3 = "mr-example@example.com";
//
////        system under test
//        var toTest = new UserService(null, null);
//
////        when
//        var result1 = toTest.validateEmail(mail1);
//        var result2 = toTest.validateEmail(mail2);
//        var result3 = toTest.validateEmail(mail3);
//
////        then
//        assertTrue(result1);
//        assertTrue(result2);
//        assertTrue(result3);
//    }
//
//    @Test
//    void validateEmail_shouldReturnFalseForInvalidEmailAddress()
//    {
////        given
//        var mail1 = "example";
//        var mail2 = "@example.com";
//        var mail3 = "exampleexample.com";
//        var mail4 = "example!example.com";
//        var mail5 = "example@example";
//
////        system under test
//        var toTest = new UserService(null, null);
//
////        when
//        var result1 = toTest.validateEmail(mail1);
//        var result2 = toTest.validateEmail(mail2);
//        var result3 = toTest.validateEmail(mail3);
//        var result4 = toTest.validateEmail(mail4);
//        var result5 = toTest.validateEmail(mail5);
//
////        then
//        assertFalse(result1);
//        assertFalse(result2);
//        assertFalse(result3);
//        assertFalse(result4);
//        assertFalse(result5);
//    }
//
//    @Test
//    void validatePassword_shouldReturnTrueForValidPassword()
//    {
////        given
//        var password1 = "1a@Z!s7y";
//        var password2 = "S1lneH@aslo";
//        var password3 = "@dmin1_Dt";
//
//
////        system under test
//        var toTest = new UserService(null, null);
//
////        when
//        var result1 = toTest.validatePassword(password1);
//        var result2 = toTest.validatePassword(password2);
//        var result3 = toTest.validatePassword(password3);
//
////        then
//        assertTrue(result1);
//        assertTrue(result2);
//        assertTrue(result3);
//    }
//
//    @Test
//    void validatePassword_shouldReturnFalseForInvalidPassword()
//    {
////        given
//        var password1 = "1a@Z";     //too short
//        var password2 = "1a@Z1a@Z1a@Z1a@Z1a@Z1a@Z1a@Z";     //too long
//        var password3 = "1A@Z1A@Z";     //without lowercase
//        var password4 = "1a@z1a@z";     //without uppercase
//        var password5 = "aA@zaA@z";     //without digit
//        var password6 = "1Aaz1Aaz";     //without special character
//        var password7 = "1A@z 1A@z";     //with space
//
////        system under test
//        var toTest = new UserService(null, null);
//
////        when
//        var result1 = toTest.validatePassword(password1);
//        var result2 = toTest.validatePassword(password2);
//        var result3 = toTest.validatePassword(password3);
//        var result4 = toTest.validatePassword(password4);
//        var result5 = toTest.validatePassword(password5);
//        var result6 = toTest.validatePassword(password6);
//        var result7 = toTest.validatePassword(password7);
//
////        then
//        assertFalse(result1);
//        assertFalse(result2);
//        assertFalse(result3);
//        assertFalse(result4);
//        assertFalse(result5);
//        assertFalse(result6);
//        assertFalse(result7);
//    }
//
//    @Test
//    void getUserByEmail_shouldReturnUserWhenEmailFound()
//    {
////        given
//        var mockUserRepo = mock(UserRepository.class);
//        given(mockUserRepo.findByEmail(anyString())).willReturn(Optional.of(new User("foo@example.com", null,
//                null, "foo")));
//
////        system under test
//        var toTest = new UserService(mockUserRepo, null);
//
////        when
//        var result = toTest.getUserByEmail("foo@example.com");
//
////        then
//        assertNotNull(result);
//        assertEquals("foo@example.com", result.getEmail());
//        assertEquals("foo", result.getName());
//    }
//
//    @Test
//    void getUserByEmail_shouldThrowExceptionWhenEmailNotFound()
//    {
////        given
//        var mockUserRepo = mock(UserRepository.class);
//        given(mockUserRepo.findByEmail(anyString())).willReturn(Optional.empty());
//
////        system under test
//        var toTest = new UserService(mockUserRepo, null);
//
////        when
//        var result = catchThrowable(() -> toTest.getUserByEmail("foo@example.com"));
//
////        then
//        assertThat(result)
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("No user with given email");
//
//    }
//
//    @Test
//    void getUserByEmailAsDto_shouldReturnUserDto()
//    {
////        test samego tworzenia dto w teście klasy userDto
////        given
//        var mockUserRepo = mock(UserRepository.class);
//        given(mockUserRepo.findByEmail(anyString())).willReturn(Optional.of(new User()));
//
////        system under test
//        var toTest = new UserService(mockUserRepo, null);
//
////        when
//        var result = toTest.getUserByEmailAsDto("foo@example.com");
//
////        then
//        assertThat(result).isInstanceOf(UserDTO.class);
//    }
//
//    @Test
//    void createUser_shouldTReturnCreatedUser()
//    {
////        given
//        var toSave = new UserDTO("foo@example.com", "S1lneH@aslo", "bar");
//
//        var mockUserRepo = mock(UserRepository.class);
//        given(mockUserRepo.existsByEmail(anyString())).willReturn(false);
//        given(mockUserRepo.save(any())).willReturn(toSave.toUser());
//
//        var mockPasswordEncoder = mock(PasswordEncoder.class);
//
////        system under test
//        var toTest = new UserService(mockUserRepo, mockPasswordEncoder);
//
//
////        when
//        var result = toTest.createUser(toSave);
//
////        then
//        assertNotNull(result);
//        assertEquals("foo@example.com", result.getEmail());
//        assertEquals("bar", result.getName());
//
//    }
//
//    @Test
//    void createUser_shouldThrowExceptionWhenUserEmailInvalid()
//    {
////        given
//        var toSave = new UserDTO("foo2example.com", "12345", "bar");
//
////        system under test
//        var toTest = new UserService(null, null);
//
////        when
//        var result = catchThrowable(() -> toTest.createUser(toSave));
////        then
//        assertThat(result)
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Invalid email");
//    }
//
//
//    @Test
//    void createUser_shouldThrowExceptionWhenUserEmailAlreadyUsed()
//    {
////        given
//        var toSave = new UserDTO("foo@example.com", "12345", "bar");
//
//        var mockUserRepo = mock(UserRepository.class);
//        given(mockUserRepo.existsByEmail(anyString())).willReturn(true);
//
////        system under test
//        var toTest = new UserService(mockUserRepo, null);
//
////        when
//        var result = catchThrowable(() -> toTest.createUser(toSave));
////        then
//        assertThat(result)
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("email already exist");
//    }
//
//    @Test
//    void createUser_shouldThrowExceptionWhenInvalidPasswordIsProvided()
//    {
////        given
//        var toSave = new UserDTO("foo@example.com", "12345", "bar");
//
//        var mockUserRepo = mock(UserRepository.class);
//        given(mockUserRepo.existsByEmail(anyString())).willReturn(false);
//
////        system under test
//        var toTest = new UserService(mockUserRepo, null);
//
////        when
//        var result = catchThrowable(() -> toTest.createUser(toSave));
////        then
//        assertThat(result)
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("password is invalid");
//    }
//
////
////    String hashPassword(String toHash)
////    {
////        return this.encoder.encode(toHash);
////    }
////
//
//    @Test
//    void getUserIdByEmail_shouldReturnUserIdWhenEmailFound()
//    {
////        given
//        var mockUserRepo = mock(UserRepository.class);
//        given(mockUserRepo.findIdByEmail(anyString())).willReturn(Optional.of(2L));
//
////        system under test
//        var toTest = new UserService(mockUserRepo, null);
//
////        when
//        var result = toTest.getUserIdByEmail("foo@example.com");
//
////        then
//        assertNotNull(result);
//        assertEquals(2L, result);
//    }
//
//    @Test
//    void getUserIdByEmail_shouldThrowExceptionWhenEmailNotFound()
//    {
////        given
//        var mockUserRepo = mock(UserRepository.class);
//        given(mockUserRepo.findIdByEmail(anyString())).willReturn(Optional.empty());
//
////        system under test
//        var toTest = new UserService(mockUserRepo, null);
//
////        when
//        var result = catchThrowable(() -> toTest.getUserIdByEmail("foo@example.com"));
//
////        then
//        assertThat(result)
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("No user with given email found");
//    }
//
//    @Test
//    void getUserById_shouldReturnUserWhenIdFound()
//    {
////        given
//        var mockUserRepo = mock(UserRepository.class);
//        given(mockUserRepo.findById(anyLong())).willReturn(Optional.of(new User("foo@example.com", null ,
//                null, "bar")));
//
////        system under test
//        var toTest = new UserService(mockUserRepo, null);
//
////        when
//        var result = toTest.getUserById(4L);
//
////        then
//        assertNotNull(result);
//        assertEquals("foo@example.com", result.getEmail());
//        assertEquals("bar", result.getName());
//    }
//
//    @Test
//    void getUserById_shouldThrowExceptionWhenIdNotFound()
//    {
////        given
//        var mockUserRepo = mock(UserRepository.class);
//        given(mockUserRepo.findById(anyLong())).willReturn(Optional.empty());
//
////        system under test
//        var toTest = new UserService(mockUserRepo, null);
//
////        when
//        var result = catchThrowable(() -> toTest.getUserById(2L));
//
////        then
//        assertThat(result)
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("No user with given id");
//
//    }
//
//    @Test
//    void getUserNameById_shouldReturnNameWhenIdFound()
//    {
////        given
//        var mockUserRepo = mock(UserRepository.class);
//        given(mockUserRepo.findNameById(anyLong())).willReturn(Optional.of("Kamil"));
//
////        system under test
//        var toTest = new UserService(mockUserRepo, null);
//
////        when
//        var result = toTest.getUserNameById(3L);
//
////        then
//        assertNotNull(result);
//        assertEquals("Kamil", result);
//    }
//
//    @Test
//    void getUserNameById_shouldThrowExceptionWhenidNotFound()
//    {
////        given
//        var mockUserRepo = mock(UserRepository.class);
//        given(mockUserRepo.findNameById(anyLong())).willReturn(Optional.empty());
//
////        system under test
//        var toTest = new UserService(mockUserRepo, null);
//
////        when
//        var result = catchThrowable(() -> toTest.getUserNameById(1L));
//
////        then
//        assertThat(result)
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("No name found");
//    }

//
//    public void deleteUserById(long userId)
//    {
//        if (!this.userRepo.existsById(userId))
//            throw new IllegalArgumentException("User with given id not found!");
//
//        this.userRepo.deleteById(userId);
//    }

}