package com.simplogics.base.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@MappedSuperclass
@Data
public class BaseEntity {
	
	@Column(name = "createdon", insertable = false, updatable = false)
	protected Date createdOn;

	@Column(name = "updatedon", insertable = false, updatable = false)
	protected Date updatedOn;
	
	@OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "createdby")
	protected User createdBy;

	@JoinColumn(name = "updatedby")
	@OneToOne(targetEntity= User.class, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	protected User updatedBy;
}
