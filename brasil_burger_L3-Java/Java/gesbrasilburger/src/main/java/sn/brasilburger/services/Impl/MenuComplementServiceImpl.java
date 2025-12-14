package sn.brasilburger.services.Impl;

import sn.brasilburger.entity.MenuComplement;
import sn.brasilburger.repository.MenuComplementRepository;
import sn.brasilburger.services.MenuComplementService;

public class MenuComplementServiceImpl implements MenuComplementService {
    private MenuComplementRepository menuComplementRepository;
    private static MenuComplementServiceImpl instance = null;
    private MenuComplementServiceImpl(MenuComplementRepository menuComplementRepository){
        this.menuComplementRepository = menuComplementRepository;
    }
    public static MenuComplementServiceImpl getInstance(MenuComplementRepository menuComplementRepository){
        if (instance==null) {
            return instance = new MenuComplementServiceImpl(menuComplementRepository);
        }
        return instance;
    }
    @Override
    public boolean createMenuComplement(MenuComplement menuC){
        return menuComplementRepository.insert(menuC);
    }
}
