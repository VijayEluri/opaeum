package org.opaeum.disablement;
 
import java.util.HashSet;
import java.util.Set;
 
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.handlers.HandlerUtil;
 
public class Activate extends AbstractHandler {
 
@Override
public Object execute(ExecutionEvent event) throws ExecutionException {
IWorkbenchActivitySupport activitySupport = HandlerUtil
.getActiveWorkbenchWindow(event).getWorkbench()
.getActivitySupport();
IActivityManager activityManager = activitySupport.getActivityManager();
Set enabledActivities = new HashSet();
String id = "de.vogella.rcp.activities.view";
if (activityManager.getActivity(id).isDefined()) {
enabledActivities.add(id);
 
}
activitySupport.setEnabledActivityIds(enabledActivities);
return null;
}
 
}