package com.in28minutes.springboot.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "users", collectionResourceRel = "users")
public interface UserRestRepository extends
		PagingAndSortingRepository<User, Long>, CrudRepository<User, Long> {
	List<User> findByRole(@Param("role") String role);

	@Override
	@RestResource(exported = false)
	<S extends User> S save(S entity);

	@Override
	@RestResource(exported = false)
	void delete(User entity);

	@Override
	@RestResource(exported = false)
	void deleteById(Long id);
}
