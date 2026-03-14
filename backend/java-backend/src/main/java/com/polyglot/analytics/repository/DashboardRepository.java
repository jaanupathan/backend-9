package com.polyglot.analytics.repository;

import com.polyglot.analytics.model.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    
    List<Dashboard> findByOwnerId(Long ownerId);
    
    List<Dashboard> findByIsPublicTrue();
    
    @Query("SELECT d FROM Dashboard d LEFT JOIN FETCH d.owner WHERE d.owner.id = :ownerId")
    List<Dashboard> findWithOwnerDetails(@Param("ownerId") Long ownerId);
}
