package sn.brasilburger.services.Impl;

import java.util.List;
import java.util.Optional;

import sn.brasilburger.entity.Menu;
import sn.brasilburger.repository.MenuRepository;
import sn.brasilburger.services.MenuService;

public class MenuServiceImpl implements MenuService{
    private MenuRepository menuRepository;
    private static MenuServiceImpl instance = null;
    private MenuServiceImpl(MenuRepository menuRepository){
        this.menuRepository = menuRepository;
    }
    public static MenuServiceImpl getInstance(MenuRepository menuRepository){
        if (instance==null) {
            return instance = new MenuServiceImpl(menuRepository);
        }
        return instance;
    }
    @Override
    public List<Menu> getAllMenu(){
        return menuRepository.findAll();
    }
    @Override
    public Optional<Menu> getMenuById(int id){
        return menuRepository.findById(id);
    }
    @Override
    public boolean createMenu(Menu menu){
        return menuRepository.insert(menu);
    }
    @Override
    public boolean updateMenu(Menu menu){
        return menuRepository.update(menu);
    }
    @Override
    public boolean archiveMenu(Menu menu){
        return menuRepository.delete(menu);
    }
}
