@TypeDef(name="TaskRequestStateResolver",typeClass=TaskRequestStateResolver.class)
@TypeDefs(value={@TypeDef(name="AbstractRequestStateResolver",typeClass=AbstractRequestStateResolver.class),@TypeDef(name="ProcessRequestStateResolver",typeClass=ProcessRequestStateResolver.class),@TypeDef(name="TaskRequestStateResolver",typeClass=TaskRequestStateResolver.class)})
@AnyMetaDefs({@AnyMetaDef(idType="long",metaValues={},name="Participant",metaType="integer"),@AnyMetaDef(idType="long",metaValues={},name="OperationProcessObject",metaType="integer"),@AnyMetaDef(idType="long",metaValues={},name="BusinessRole",metaType="integer"),@AnyMetaDef(idType="long",metaValues={},name="RequestObject",metaType="integer"),@AnyMetaDef(idType="long",metaValues={},name="BusinessComponent",metaType="integer"),@AnyMetaDef(idType="long",metaValues={},name="TaskObject",metaType="integer"),@AnyMetaDef(idType="long",metaValues={},name="TaskObjectMetaDef",metaType="integer"),@AnyMetaDef(idType="long",metaValues={},name="OperationProcessMetaDef",metaType="integer"),@AnyMetaDef(idType="long",metaValues={},name="ParticipantMetaDef",metaType="integer")})
@FilterDef(defaultCondition="deleted_on > current_timestamp",name="noDeletedObjects")
@NamedQueries(value=@NamedQuery(query="select processInstanceInfo.processInstanceId from ProcessInstanceInfo processInstanceInfo where :type in elements(processInstanceInfo.eventTypes)",name="ProcessInstancesWaitingForEvent"))
package org.nakeduml.runtime.bpm.util;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.AnyMetaDefs;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.nakeduml.runtime.bpm.AbstractRequestStateResolver;
import org.nakeduml.runtime.bpm.ProcessRequestStateResolver;
import org.nakeduml.runtime.bpm.TaskRequestStateResolver;

