package com.guestbook.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guestbook.entity.Entry;

@Repository
public interface GuestEntryRepository extends JpaRepository<Entry, Long>{

}
