package com.faust.ticketing.core.repository;

import com.faust.ticketing.persistence.model.entity.user.Group;

import javax.ejb.Stateless;

@Stateless
public class GroupRepository extends JPACrudRepository<Group> {
}
