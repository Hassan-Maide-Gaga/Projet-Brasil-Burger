package sn.brasilburger.services.Impl;

import java.util.List;
import java.util.Optional;

import sn.brasilburger.entity.Burger;
import sn.brasilburger.entity.Menu;
import sn.brasilburger.repository.BurgerRepository;
import sn.brasilburger.services.BurgerService;

public class BurgerServiceImpl implements BurgerService{
    private BurgerRepository burgerRepository;
    private static BurgerServiceImpl instance = null;
    private BurgerServiceImpl(BurgerRepository burgerRepository){
        this.burgerRepository = burgerRepository;
    }
    public static BurgerServiceImpl getInstance(BurgerRepository burgerRepository){
        if (instance==null) {
            return instance = new BurgerServiceImpl(burgerRepository);
        }
        return instance;
    }
    @Override
    public List<Burger> getAllBurger(){
        return burgerRepository.findAll();
    }
    @Override
    public Optional<Burger> getBurgerById(int id){
        return burgerRepository.findById(id);
    }
    @Override
    public boolean createBurger(Burger burger){
        return burgerRepository.insert(burger);
    }
    @Override
    public boolean updateBurger(Burger burger){
        return burgerRepository.update(burger);
    }
    @Override
    public boolean archiveBurger(Burger burger){
        return burgerRepository.delete(burger);
    }
    @Override
    public Optional<Burger> getBurgerByMenu(Menu menu){
        return burgerRepository.getBurgerByMenu(menu);
    }
}
