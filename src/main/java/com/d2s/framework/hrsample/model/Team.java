/*
 * Generated by Design2see. All rights reserved.
 */
package com.d2s.framework.hrsample.model;

/**
 * Team entity.
 * <p>
 * Generated by Design2see. All rights reserved.
 * <p>
 *
 * @hibernate.mapping
 *           default-access = "com.d2s.framework.model.persistence.hibernate.property.EntityPropertyAccessor"
 * @hibernate.joined-subclass
 *           table = "TEAM"
 *           dynamic-insert = "true"
 *           dynamic-update = "true"
 *           persister = "com.d2s.framework.model.persistence.hibernate.entity.persister.EntityProxyJoinedSubclassEntityPersister"
 * @hibernate.joined-subclass-key
 *           column = "ID"
 * @author Generated by Design2see
 * @version $LastChangedRevision$
 */
public interface Team extends
  com.d2s.framework.hrsample.model.OrganizationalUnit,
  com.d2s.framework.model.entity.IEntity {

  // THIS IS JUST A MARKER INTERFACE.
}