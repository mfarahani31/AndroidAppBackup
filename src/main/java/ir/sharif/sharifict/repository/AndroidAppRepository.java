package ir.sharif.sharifict.repository;

import ir.sharif.sharifict.model.AndroidApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AndroidAppRepository extends JpaRepository<AndroidApp,String> {

}
