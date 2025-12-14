package sn.brasilburger.services.Impl;

import java.util.List;
import java.util.Optional;

import sn.brasilburger.entity.User;
import sn.brasilburger.repository.LoginRepository;
import sn.brasilburger.services.LoginService;

public class LoginServiceImpl implements LoginService{
    private LoginRepository loginRepository;
    private static LoginServiceImpl instance = null;
    private LoginServiceImpl(LoginRepository loginRepository){
        this.loginRepository = loginRepository;
    }
    public static LoginServiceImpl getInstance(LoginRepository loginRepository){
        if (instance==null) {
            return instance = new LoginServiceImpl(loginRepository);
        }
        return instance;
    }
    @Override
    public Optional<User> login(String email, String pwd){
        return loginRepository.getUserByEmail(email)
            .filter(user -> user.getPassword().equals(pwd));
    }
    @Override
    public Optional<User> getGesByMail(String email){
        return loginRepository.getUserByEmail(email)
            .filter(user -> user.getRole().toString().equals("GESTIONNAIRE"));
    }
    @Override
    public boolean signup(User user){
        return loginRepository.insert(user);
    }
    @Override
    public boolean updateGes(User user){
        return loginRepository.update(user);
    }
    @Override
    public boolean archiveGes(User user){
        return loginRepository.delete(user);
    }
    @Override
    public List<User> getAllGes(){
        return loginRepository.getAll();
    }
}
