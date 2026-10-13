package andrei.usersservice.service;


import andrei.usersservice.model.User;

public interface IUserCommandService {
    User register(String username, String email, String password);
    boolean updateUserRole(long id, String userRole);
    boolean updatePassword(long id, String password);
    boolean delete(long id);
}
