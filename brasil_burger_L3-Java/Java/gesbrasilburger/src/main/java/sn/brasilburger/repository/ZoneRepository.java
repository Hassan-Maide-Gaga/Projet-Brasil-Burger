package sn.brasilburger.repository;
import  sn.brasilburger.entity.*;
import java.util.List;
import java.util.Optional;

public interface ZoneRepository {
    List<Zone> findAll();
    Optional<Zone> findById(int id);
    boolean insert(Zone zone);
    boolean update(Zone zone);
    boolean delete(Zone zone);
}
