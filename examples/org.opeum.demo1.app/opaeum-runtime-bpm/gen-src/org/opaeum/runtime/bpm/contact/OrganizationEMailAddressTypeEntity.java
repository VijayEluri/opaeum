package org.opaeum.runtime.bpm.contact;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.opaeum.hibernate.domain.AbstractPersistentOpaeumIdEnum;

@Table(name="organization_e_mail_address_type")
@Entity(name="OrganizationEMailAddressTypeEntity")
public class OrganizationEMailAddressTypeEntity extends AbstractPersistentOpaeumIdEnum {


	/** Constructor for OrganizationEMailAddressTypeEntity
	 * 
	 * @param e 
	 */
	public OrganizationEMailAddressTypeEntity(OrganizationEMailAddressType e) {
		super(e);
	}
	
	/** Default constructor for OrganizationEMailAddressTypeEntity
	 */
	public OrganizationEMailAddressTypeEntity() {
	}


}