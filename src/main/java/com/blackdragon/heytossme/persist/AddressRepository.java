package com.blackdragon.heytossme.persist;

import com.blackdragon.heytossme.persist.entity.Address;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query(nativeQuery = true, value = "select distinct first_depth_region, second_depth_region from address as a")
    List<List<String>> findDistinct();
}
