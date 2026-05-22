package com.faust.ticketing.core.repository;

import com.faust.ticketing.core.graph.GraphHelper;
import com.faust.ticketing.persistence.model.entity.ticket.Category;
import com.faust.ticketing.persistence.sql.NamedEntityGraph;
import com.faust.ticketing.persistence.sql.NamedQuery;

import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.Set;

@Stateless
public class CategoryRepository extends JPACrudRepository<Category> {
}
