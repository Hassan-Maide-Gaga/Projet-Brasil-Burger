package sn.brasilburger.config.factory.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import  sn.brasilburger.config.factory.EntityName;
import  sn.brasilburger.config.factory.repository.RepositoryFactory;
import  sn.brasilburger.repository.BurgerRepository;
import  sn.brasilburger.repository.ComplementRepository;
import sn.brasilburger.repository.LivreurRepository;
import sn.brasilburger.repository.LoginRepository;
import sn.brasilburger.repository.MenuComplementRepository;
import sn.brasilburger.repository.MenuRepository;
import sn.brasilburger.repository.ZoneRepository;
import sn.brasilburger.services.Impl.BurgerServiceImpl;
import sn.brasilburger.services.Impl.ComplementServiceImpl;
import sn.brasilburger.services.Impl.ImageUploadServiceImpl;
import sn.brasilburger.services.Impl.LivreurServiceImpl;
import sn.brasilburger.services.Impl.LoginServiceImpl;
import sn.brasilburger.services.Impl.MenuComplementServiceImpl;
import sn.brasilburger.services.Impl.MenuServiceImpl;
import sn.brasilburger.services.Impl.ZoneServiceImpl;

public final  class ServicesFactory {
    private ServicesFactory(){}
    public static Object createServices(EntityName entity){
        switch (entity) {
            case BURGER:
                BurgerRepository burgerRepo = (BurgerRepository)RepositoryFactory.createRepository(entity);
                return BurgerServiceImpl.getInstance(burgerRepo);
            case COMPLEMENT:
                ComplementRepository complementRepo = (ComplementRepository)RepositoryFactory.createRepository(entity);
                return ComplementServiceImpl.getInstance(complementRepo);
            case LIVREUR:
                LivreurRepository livreurRepo = (LivreurRepository)RepositoryFactory.createRepository(entity);
                return LivreurServiceImpl.getInstance(livreurRepo);
            case LOGIN:
                LoginRepository loginRepo = (LoginRepository)RepositoryFactory.createRepository(entity);
                return LoginServiceImpl.getInstance(loginRepo);
            case MENU:
                MenuRepository menuRepo = (MenuRepository)RepositoryFactory.createRepository(entity);
                return MenuServiceImpl.getInstance(menuRepo);
            case ZONE:
                ZoneRepository zoneRepo = (ZoneRepository)RepositoryFactory.createRepository(entity);
                return ZoneServiceImpl.getInstance(zoneRepo);
            case MENU_COMPLEMENT:
                MenuComplementRepository menuComplementRepo = (MenuComplementRepository)RepositoryFactory.createRepository(entity);
                return MenuComplementServiceImpl.getInstance(menuComplementRepo);
            case IMAGE_UPLOAD:
                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "drynxwp0h",
                        "api_key", "118422941322141",
                        "api_secret", "jfROTFudw-cFwHrZdqDis2C9wD8"
                ));
                return ImageUploadServiceImpl.getInstance(cloudinary);
            default:
            throw new IllegalArgumentException("Unknow entity: "+ entity);
        }
    }
}

