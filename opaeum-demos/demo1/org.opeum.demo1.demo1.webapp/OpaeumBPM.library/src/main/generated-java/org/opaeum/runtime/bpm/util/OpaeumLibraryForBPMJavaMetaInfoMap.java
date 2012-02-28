package org.opaeum.runtime.bpm.util;

import org.opaeum.runtime.domain.IPersistentObject;
import org.opaeum.runtime.environment.JavaMetaInfoMap;

public class OpaeumLibraryForBPMJavaMetaInfoMap extends JavaMetaInfoMap {
	static final public OpaeumLibraryForBPMJavaMetaInfoMap INSTANCE = new OpaeumLibraryForBPMJavaMetaInfoMap();

	/** Constructor for OpaeumLibraryForBPMJavaMetaInfoMap
	 */
	public OpaeumLibraryForBPMJavaMetaInfoMap() {
		putClass(org.opaeum.runtime.bpm.businesscalendar.BusinessCalendar.class,"252060@_x9fmQNb9EeCJ0dmaHEVVnw");
		putMethod(org.opaeum.runtime.bpm.businesscalendar.BusinessCalendar.class,"252060@_NTccANcEEeCJ0dmaHEVVnw",1046973307095054425l);
		putMethod(org.opaeum.runtime.bpm.businesscalendar.BusinessCalendar.class,"252060@_dXLYsASTEeGb9qsDxKJdSA",460894547451043801l);
		putMethod(org.opaeum.runtime.bpm.businesscalendar.BusinessCalendar.class,"252060@_mOhZgNcEEeCJ0dmaHEVVnw",1668962487164386505l);
		putClass(org.opaeum.runtime.bpm.businesscalendar.CronExpression.class,"252060@_hqpgYASQEeGb9qsDxKJdSA");
		putClass(java.util.Date.class,"252060@_qJQboNcCEeCJ0dmaHEVVnw");
		putClass(java.lang.Integer.class,"252060@_FvE9kNcCEeCJ0dmaHEVVnw");
		putClass(org.opaeum.runtime.bpm.businesscalendar.Duration.class,"252060@_mkwNcASREeGb9qsDxKJdSA");
		putClass(java.lang.Integer.class,"252060@_bb59wNb_EeCJ0dmaHEVVnw");
		putClass(java.lang.Integer.class,"252060@_cIJYsNb_EeCJ0dmaHEVVnw");
		putClass(org.opaeum.runtime.bpm.businesscalendar.Month.class,"252060@_VSZhgNcCEeCJ0dmaHEVVnw");
		putClass(org.opaeum.runtime.bpm.businesscalendar.OnceOffHoliday.class,"252060@_5rW3kNcCEeCJ0dmaHEVVnw");
		putClass(org.opaeum.runtime.bpm.businesscalendar.RecurringHoliday.class,"252060@_TFKVQNb_EeCJ0dmaHEVVnw");
		putClass(org.opaeum.runtime.bpm.businesscalendar.TimeOfDay.class,"252060@_UjTHMNb_EeCJ0dmaHEVVnw");
		putClass(org.opaeum.runtime.bpm.businesscalendar.WorkDay.class,"252060@_Jn9QcNb-EeCJ0dmaHEVVnw");
		putClass(org.opaeum.runtime.bpm.businesscalendar.WorkDayKind.class,"252060@_EnGlsNb-EeCJ0dmaHEVVnw");
		putClass(org.opaeum.runtime.bpm.contact.Address.class,"252060@_eIdasF-jEeGSPaWW9iQb9Q");
		putClass(org.opaeum.runtime.bpm.contact.EMailAddress.class,"252060@_r-4aMEtmEeGd4cpyhpib9Q");
		putClass(org.opaeum.runtime.bpm.contact.IPersonAddress.class,"252060@_EpwdoF-hEeGSPaWW9iQb9Q");
		putClass(org.opaeum.runtime.bpm.contact.IPersonEMailAddress.class,"252060@_9gswQF-gEeGSPaWW9iQb9Q");
		putClass(org.opaeum.runtime.bpm.contact.IPersonPhoneNumber.class,"252060@_DY-LMF-hEeGSPaWW9iQb9Q");
		putClass(org.opaeum.runtime.bpm.contact.OrganizationEMailAddress.class,"252060@_GfviYEtqEeGd4cpyhpib9Q");
		putClass(org.opaeum.runtime.bpm.contact.OrganizationEMailAddressType.class,"252060@_58k8wEtpEeGd4cpyhpib9Q");
		putClass(org.opaeum.runtime.bpm.contact.OrganizationPhoneNumber.class,"252060@_Ca9wQEtoEeGd4cpyhpib9Q");
		putClass(org.opaeum.runtime.bpm.contact.OrganizationPhoneNumberType.class,"252060@_uImqUEtnEeGd4cpyhpib9Q");
		putClass(org.opaeum.runtime.bpm.contact.PersonEMailAddress.class,"252060@_LSLzIEtpEeGd4cpyhpib9Q");
		putClass(org.opaeum.runtime.bpm.contact.PersonEMailAddressType.class,"252060@_YLfSwEtnEeGd4cpyhpib9Q");
		putClass(org.opaeum.runtime.bpm.contact.PersonPhoneNumber.class,"252060@_3E_9kEtnEeGd4cpyhpib9Q");
		putClass(org.opaeum.runtime.bpm.contact.PersonPhoneNumberType.class,"252060@_Z-VwcEtnEeGd4cpyhpib9Q");
		putClass(org.opaeum.runtime.bpm.contact.PhoneNumber.class,"252060@_qzasoEtmEeGd4cpyhpib9Q");
		putClass(org.opaeum.runtime.bpm.contact.PhysicalAddress.class,"252060@_cD7-MF-jEeGSPaWW9iQb9Q");
		putClass(org.opaeum.runtime.bpm.contact.PostalAddress.class,"252060@_anU2QF-jEeGSPaWW9iQb9Q");
		putClass(org.opaeum.runtime.bpm.document.IBusinessDocument.class,"252060@_cOzTkF9lEeG3X_yvufTVmw");
		putMethod(org.opaeum.runtime.bpm.document.IBusinessDocument.class,"252060@_nx6xcF9lEeG3X_yvufTVmw",4293337385271490119l);
		putClass(org.opaeum.runtime.bpm.document.INotification.class,"252060@_G-a2oF-bEeGSPaWW9iQb9Q");
		putClass(org.opaeum.runtime.bpm.organization.BusinessNetwork.class,"252060@_NRu9QFYjEeGJUqEGX7bKSg");
		putClass(org.opaeum.runtime.bpm.organization.BusinessNetworkFacilatatesCollaboration.class,"252060@_YJGvcFYjEeGJUqEGX7bKSg");
		putClass(org.opaeum.runtime.bpm.organization.IBusiness.class,"252060@_G6MA0FYtEeGj5_I7bIwNoA");
		putClass(org.opaeum.runtime.bpm.organization.IBusinessActor.class,"252060@_AN3QcEtxEeGElKTCe2jfDw");
		putClass(org.opaeum.runtime.organization.IBusinessActorBase.class,"252060@_KfH2gGG6EeGY4pS7YATNzA");
		putClass(org.opaeum.runtime.organization.IBusinessBase.class,"252060@_MVFSkGG6EeGY4pS7YATNzA");
		putClass(org.opaeum.runtime.bpm.organization.IBusinessCollaboration.class,"252060@_zGMYgEvREeGmqIr8YsFD4g");
		putClass(org.opaeum.runtime.organization.IBusinessCollaborationBase.class,"252060@_NsvpEGG6EeGY4pS7YATNzA");
		putClass(org.opaeum.runtime.bpm.organization.IBusinessComponent.class,"252060@_uVek8IoVEeCLqpffVZYAlw");
		putClass(org.opaeum.runtime.organization.IBusinessNetwork.class,"252060@_6e-W8F13EeGKRdKItFLDfg");
		putClass(org.opaeum.runtime.bpm.organization.IBusinessRole.class,"252060@_tH0fAIoVEeCLqpffVZYAlw");
		putClass(org.opaeum.runtime.bpm.organization.IBusinessService.class,"252060@_bVBY0F-aEeGSPaWW9iQb9Q");
		putClass(org.opaeum.runtime.organization.IOrganizationNode.class,"252060@_8ItU4F13EeGKRdKItFLDfg");
		putClass(org.opaeum.runtime.organization.IPerson.class,"252060@_7c1tYF13EeGKRdKItFLDfg");
		putClass(org.opaeum.runtime.bpm.organization.Leave.class,"252060@_Pybi0Et3EeGElKTCe2jfDw");
		putClass(org.opaeum.runtime.bpm.organization.OrganizationFullfillsActorRole.class,"252060@_WjvQ0EtyEeGElKTCe2jfDw");
		putClass(org.opaeum.runtime.bpm.organization.OrganizationNode.class,"252060@_pZdQEEtmEeGd4cpyhpib9Q");
		putClass(org.opaeum.runtime.bpm.organization.Participant.class,"252060@_YgstsI29EeCrtavWRHwoHg");
		putClass(org.opaeum.runtime.bpm.organization.PersonFullfillsActorRole.class,"252060@_X4-lcEtyEeGElKTCe2jfDw");
		putClass(org.opaeum.runtime.bpm.organization.PersonNode.class,"252060@_k23OoEtmEeGd4cpyhpib9Q");
		putClass(org.opaeum.runtime.bpm.organization.Organization_iBusinessComponent_1.class,"252060@_vf4noFYuEeGj5_I7bIwNoA");
		putClass(org.opaeum.runtime.bpm.organization.Person_iBusinessRole_1.class,"252060@_3lcZgFYuEeGj5_I7bIwNoA");
		putClass(org.opaeum.runtime.bpm.request.AbstractRequest.class,"252060@_6MA8UI2-EeCrtavWRHwoHg");
		putMethod(org.opaeum.runtime.bpm.request.AbstractRequest.class,"252060@_-PsMoIoaEeCPduia_-NbFw",2809139800710243133l);
		putMethod(org.opaeum.runtime.bpm.request.AbstractRequest.class,"252060@_3USwcKDGEeCv9IRqC7lfYw",3717858776997870408l);
		putMethod(org.opaeum.runtime.bpm.request.AbstractRequest.class,"252060@_4RNcAIoaEeCPduia_-NbFw",20843194362239925l);
		putMethod(org.opaeum.runtime.bpm.request.AbstractRequest.class,"252060@_Nl5kQI6SEeCrtavWRHwoHg",4679685790909812900l);
		putMethod(org.opaeum.runtime.bpm.request.AbstractRequest.class,"252060@_Qo338I6QEeCrtavWRHwoHg",547906873733717658l);
		putMethod(org.opaeum.runtime.bpm.request.AbstractRequest.class,"252060@_VJyB4I6REeCrtavWRHwoHg",5133633174525298670l);
		putMethod(org.opaeum.runtime.bpm.request.AbstractRequest.class,"252060@_ov5DMIoaEeCPduia_-NbFw",3547071820132893959l);
		putMethod(org.opaeum.runtime.bpm.request.AbstractRequest.class,"252060@_qwWfEIoaEeCPduia_-NbFw",286517672851676135l);
		putClass(org.opaeum.runtime.bpm.request.Participation.class,"252060@_jRjnII6MEeCrtavWRHwoHg");
		putClass(org.opaeum.runtime.bpm.request.ParticipationInRequest.class,"252060@_GVhgMI6NEeCrtavWRHwoHg");
		putClass(org.opaeum.runtime.bpm.request.ParticipationInTask.class,"252060@_vZOC4I6UEeCne5ArYLDbiA");
		putClass(org.opaeum.runtime.bpm.request.ProcessRequest.class,"252060@_ciiWAI2-EeCrtavWRHwoHg");
		putMethod(org.opaeum.runtime.bpm.request.ProcessRequest.class,"252060@_4zDaYK0wEeCTTvcJZSDicw",3600202940411078804l);
		putClass(org.opaeum.runtime.bpm.request.RequestParticipationKind.class,"252060@_ysdO4I6MEeCrtavWRHwoHg");
		putClass(org.opaeum.runtime.bpm.request.TaskParticipationKind.class,"252060@_neCVAI6UEeCne5ArYLDbiA");
		putClass(org.opaeum.runtime.bpm.request.TaskRequest.class,"252060@_zFmsEIoVEeCLqpffVZYAlw");
		putMethod(org.opaeum.runtime.bpm.request.TaskRequest.class,"252060@_0lAQAIoaEeCPduia_-NbFw",6837298467385087869l);
		putMethod(org.opaeum.runtime.bpm.request.TaskRequest.class,"252060@_1gF8AKDTEeCi16HgBnUGFw",2519565167906952379l);
		putMethod(org.opaeum.runtime.bpm.request.TaskRequest.class,"252060@_GRVH0IobEeCPduia_-NbFw",8236205458182045891l);
		putMethod(org.opaeum.runtime.bpm.request.TaskRequest.class,"252060@_LlMOIIobEeCPduia_-NbFw",7253144136017044923l);
		putMethod(org.opaeum.runtime.bpm.request.TaskRequest.class,"252060@_Nk_isIobEeCPduia_-NbFw",7608483210400824401l);
		putMethod(org.opaeum.runtime.bpm.request.TaskRequest.class,"252060@__6uyIIoaEeCPduia_-NbFw",7251280809563715157l);
		putMethod(org.opaeum.runtime.bpm.request.TaskRequest.class,"252060@_v52VoI6SEeCrtavWRHwoHg",5654054153376055834l);
		putMethod(org.opaeum.runtime.bpm.request.TaskRequest.class,"252060@_wuzAoI6SEeCrtavWRHwoHg",3079422596157096726l);
		putClass(org.opaeum.runtime.bpm.requestobject.IProcessObject.class,"252060@_5DVD4I3oEeCfQedkc0TCdA");
		putMethod(org.opaeum.runtime.bpm.requestobject.IProcessObject.class,"252060@_CDJHUEuCEeGElKTCe2jfDw",3971273529216092426l);
		putMethod(org.opaeum.runtime.bpm.requestobject.IProcessObject.class,"252060@_v7N4oEuBEeGElKTCe2jfDw",4371667695595476508l);
		putMethod(org.opaeum.runtime.bpm.requestobject.IProcessObject.class,"252060@_xCSOYEuBEeGElKTCe2jfDw",7740529359203122672l);
		putMethod(org.opaeum.runtime.bpm.requestobject.IProcessObject.class,"252060@_xd064EuBEeGElKTCe2jfDw",3575040635504978288l);
		putMethod(org.opaeum.runtime.bpm.requestobject.IProcessObject.class,"252060@_yDSYYEuBEeGElKTCe2jfDw",267318476342637340l);
		putClass(org.opaeum.runtime.bpm.requestobject.IRequestObject.class,"252060@_Wd2QoI53EeCfQedkc0TCdA");
		putClass(org.opaeum.runtime.bpm.requestobject.IResponsibilityInvocation.class,"252060@_2nANcEt8EeGElKTCe2jfDw");
		putClass(org.opaeum.runtime.bpm.requestobject.IResponsibilityProcessObject.class,"252060@_w0UdIEt8EeGElKTCe2jfDw");
		putClass(org.opaeum.runtime.bpm.requestobject.IResponsibilityTaskObject.class,"252060@_z_v3UEt8EeGElKTCe2jfDw");
		putClass(org.opaeum.runtime.bpm.requestobject.ITaskObject.class,"252060@_2tdYsI3oEeCfQedkc0TCdA");
		putMethod(org.opaeum.runtime.bpm.requestobject.ITaskObject.class,"252060@_8ba9IK0NEeCK48ywUpk_rg",2058820369700246458l);
		putMethod(org.opaeum.runtime.bpm.requestobject.ITaskObject.class,"252060@_EE4B0K0OEeCK48ywUpk_rg",6478136403880983034l);
		putMethod(org.opaeum.runtime.bpm.requestobject.ITaskObject.class,"252060@_NdLN8K0OEeCK48ywUpk_rg",4515354755132661098l);
		putMethod(org.opaeum.runtime.bpm.requestobject.ITaskObject.class,"252060@_XbPZkK0OEeCK48ywUpk_rg",1858551074079807460l);
		putMethod(org.opaeum.runtime.bpm.requestobject.ITaskObject.class,"252060@__imwgK0NEeCK48ywUpk_rg",6501096811931843070l);
		putMethod(org.opaeum.runtime.bpm.requestobject.ITaskObject.class,"252060@_a_82cK0OEeCK48ywUpk_rg",8469502165435790444l);
		putMethod(org.opaeum.runtime.bpm.requestobject.ITaskObject.class,"252060@_fdkRQK0OEeCK48ywUpk_rg",5307650706078019150l);
		putMethod(org.opaeum.runtime.bpm.requestobject.ITaskObject.class,"252060@_iBCwEK0NEeCK48ywUpk_rg",8267495570932079064l);
		putMethod(org.opaeum.runtime.bpm.requestobject.ITaskObject.class,"252060@_qTa18K0NEeCK48ywUpk_rg",8965886927666213438l);
		putMethod(org.opaeum.runtime.bpm.requestobject.ITaskObject.class,"252060@_ug7_QK0NEeCK48ywUpk_rg",1581487435816793754l);
		putMethod(org.opaeum.runtime.bpm.requestobject.ITaskObject.class,"252060@_zwcxEK0NEeCK48ywUpk_rg",2702047623996785310l);
	}


}