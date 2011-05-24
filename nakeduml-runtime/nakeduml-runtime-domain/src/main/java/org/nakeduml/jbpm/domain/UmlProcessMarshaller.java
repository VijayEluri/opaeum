package org.nakeduml.jbpm.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.marshalling.ObjectMarshallingStrategy;
import org.drools.marshalling.impl.MarshallerReaderContext;
import org.drools.marshalling.impl.MarshallerWriteContext;
import org.drools.marshalling.impl.PersisterEnums;
import org.drools.runtime.process.NodeInstance;
import org.drools.runtime.process.NodeInstanceContainer;
import org.drools.runtime.process.WorkflowProcessInstance;
import org.jbpm.marshalling.impl.AbstractProcessInstanceMarshaller;
import org.jbpm.process.core.Context;
import org.jbpm.process.core.context.exclusive.ExclusiveGroup;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.ContextInstance;
import org.jbpm.process.instance.context.exclusive.ExclusiveGroupInstance;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.workflow.core.impl.WorkflowProcessImpl;
import org.jbpm.workflow.instance.impl.NodeInstanceImpl;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;
import org.jbpm.workflow.instance.node.CompositeContextNodeInstance;
import org.jbpm.workflow.instance.node.DynamicNodeInstance;
import org.jbpm.workflow.instance.node.EventNodeInstance;
import org.jbpm.workflow.instance.node.ForEachNodeInstance;
import org.jbpm.workflow.instance.node.HumanTaskNodeInstance;
import org.jbpm.workflow.instance.node.JoinInstance;
import org.jbpm.workflow.instance.node.MilestoneNodeInstance;
import org.jbpm.workflow.instance.node.RuleSetNodeInstance;
import org.jbpm.workflow.instance.node.StateNodeInstance;
import org.jbpm.workflow.instance.node.SubProcessNodeInstance;
import org.jbpm.workflow.instance.node.TimerNodeInstance;
import org.jbpm.workflow.instance.node.WorkItemNodeInstance;

public class UmlProcessMarshaller extends AbstractProcessInstanceMarshaller {
	public UmlProcessMarshaller (){
		
	}
	@Override
	protected WorkflowProcessInstanceImpl createProcessInstance() {
		return new RuleFlowProcessInstance();
	}
	@Override
	protected void writeNodeInstanceContent(ObjectOutputStream stream,
            NodeInstance nodeInstance, MarshallerWriteContext context)
            throws IOException {
        if (nodeInstance instanceof RuleSetNodeInstance) {
            stream.writeShort(PersisterEnums.RULE_SET_NODE_INSTANCE);
            List<Long> timerInstances =
                ((RuleSetNodeInstance) nodeInstance).getTimerInstances();
	        if (timerInstances != null) {
	            stream.writeInt(timerInstances.size());
	            for (Long id : timerInstances) {
	                stream.writeLong(id);
	            }
	        } else {
	            stream.writeInt(0);
	        }
        } else if (nodeInstance instanceof HumanTaskNodeInstance) {
            stream.writeShort(PersisterEnums.HUMAN_TASK_NODE_INSTANCE);
            stream.writeLong(((HumanTaskNodeInstance) nodeInstance).getWorkItemId());
            List<Long> timerInstances =
                ((HumanTaskNodeInstance) nodeInstance).getTimerInstances();
	        if (timerInstances != null) {
	            stream.writeInt(timerInstances.size());
	            for (Long id : timerInstances) {
	                stream.writeLong(id);
	            }
	        } else {
	            stream.writeInt(0);
	        }
        } else if (nodeInstance instanceof WorkItemNodeInstance) {
            stream.writeShort(PersisterEnums.WORK_ITEM_NODE_INSTANCE);
            stream.writeLong(((WorkItemNodeInstance) nodeInstance).getWorkItemId());
            List<Long> timerInstances =
                ((WorkItemNodeInstance) nodeInstance).getTimerInstances();
	        if (timerInstances != null) {
	            stream.writeInt(timerInstances.size());
	            for (Long id : timerInstances) {
	                stream.writeLong(id);
	            }
	        } else {
	            stream.writeInt(0);
	        }
        } else if (nodeInstance instanceof SubProcessNodeInstance) {
            stream.writeShort(PersisterEnums.SUB_PROCESS_NODE_INSTANCE);
            stream.writeLong(((SubProcessNodeInstance) nodeInstance).getProcessInstanceId());
            List<Long> timerInstances =
                ((SubProcessNodeInstance) nodeInstance).getTimerInstances();
	        if (timerInstances != null) {
	            stream.writeInt(timerInstances.size());
	            for (Long id : timerInstances) {
	                stream.writeLong(id);
	            }
	        } else {
	            stream.writeInt(0);
	        }
        } else if (nodeInstance instanceof MilestoneNodeInstance) {
            stream.writeShort(PersisterEnums.MILESTONE_NODE_INSTANCE);
            List<Long> timerInstances =
                    ((MilestoneNodeInstance) nodeInstance).getTimerInstances();
            if (timerInstances != null) {
                stream.writeInt(timerInstances.size());
                for (Long id : timerInstances) {
                    stream.writeLong(id);
                }
            } else {
                stream.writeInt(0);
            }
        } else if (nodeInstance instanceof EventNodeInstance) {
        	stream.writeShort(PersisterEnums.EVENT_NODE_INSTANCE);
    	} else if (nodeInstance instanceof TimerNodeInstance) {
            stream.writeShort(PersisterEnums.TIMER_NODE_INSTANCE);
            stream.writeLong(((TimerNodeInstance) nodeInstance).getTimerId());
        } else if (nodeInstance instanceof JoinInstance) {
            stream.writeShort(PersisterEnums.JOIN_NODE_INSTANCE);
            Map<Long, Integer> triggers = ((JoinInstance) nodeInstance).getTriggers();
            stream.writeInt(triggers.size());
            List<Long> keys = new ArrayList<Long>(triggers.keySet());
            Collections.sort(keys,
                    new Comparator<Long>() {

                        public int compare(Long o1,
                                Long o2) {
                            return o1.compareTo(o2);
                        }
                    });
            for (Long key : keys) {
                stream.writeLong(key);
                stream.writeInt(triggers.get(key));
            }
        } else if (nodeInstance instanceof StateNodeInstance) {
            stream.writeShort(PersisterEnums.STATE_NODE_INSTANCE);
            List<Long> timerInstances =
                    ((StateNodeInstance) nodeInstance).getTimerInstances();
            if (timerInstances != null) {
                stream.writeInt(timerInstances.size());
                for (Long id : timerInstances) {
                    stream.writeLong(id);
                }
            } else {
                stream.writeInt(0);
            }
        } else if (nodeInstance instanceof CompositeContextNodeInstance) {
        	if (nodeInstance instanceof DynamicNodeInstance) {
                stream.writeShort(PersisterEnums.DYNAMIC_NODE_INSTANCE);
        	} else {
        		stream.writeShort(PersisterEnums.COMPOSITE_NODE_INSTANCE);
        	}
            CompositeContextNodeInstance compositeNodeInstance = (CompositeContextNodeInstance) nodeInstance;
            List<Long> timerInstances =
                ((CompositeContextNodeInstance) nodeInstance).getTimerInstances();
            if (timerInstances != null) {
                stream.writeInt(timerInstances.size());
                for (Long id : timerInstances) {
                    stream.writeLong(id);
                }
            } else {
                stream.writeInt(0);
            }
            VariableScopeInstance variableScopeInstance = (VariableScopeInstance) compositeNodeInstance.getContextInstance(VariableScope.VARIABLE_SCOPE);
            if (variableScopeInstance == null) {
            	stream.writeInt(0);
            } else {
	            Map<String, Object> variables = variableScopeInstance.getVariables();
	            List<String> keys = new ArrayList<String>(variables.keySet());
	            Collections.sort(keys,
	                    new Comparator<String>() {
	                        public int compare(String o1,
	                                String o2) {
	                            return o1.compareTo(o2);
	                        }
	                    });
	            stream.writeInt(keys.size());
	            for (String key : keys) {
	                stream.writeUTF(key);
	                Object object=variables.get(key);
	                int index = context.objectMarshallingStrategyStore.getStrategy(object);
	                stream.writeInt(index);
	                ObjectMarshallingStrategy strategy = context.objectMarshallingStrategyStore.getStrategy(index);
	                if(strategy.accept(object)){
	                    strategy.write(stream, object);
	                }
	            }
            }
            List<NodeInstance> nodeInstances = new ArrayList<NodeInstance>(compositeNodeInstance.getNodeInstances());
            Collections.sort(nodeInstances,
                    new Comparator<NodeInstance>() {

                        public int compare(NodeInstance o1,
                                NodeInstance o2) {
                            return (int) (o1.getId() - o2.getId());
                        }
                    });
            for (NodeInstance subNodeInstance : nodeInstances) {
                stream.writeShort(PersisterEnums.NODE_INSTANCE);
                writeNodeInstance(context,
                        subNodeInstance);
            }
            stream.writeShort(PersisterEnums.END);
            List<ContextInstance> exclusiveGroupInstances =
            	compositeNodeInstance.getContextInstances(ExclusiveGroup.EXCLUSIVE_GROUP);
            if (exclusiveGroupInstances == null) {
            	stream.writeInt(0);
            } else {
            	stream.writeInt(exclusiveGroupInstances.size());
            	for (ContextInstance contextInstance: exclusiveGroupInstances) {
            		ExclusiveGroupInstance exclusiveGroupInstance = (ExclusiveGroupInstance) contextInstance;
            		Collection<NodeInstance> groupNodeInstances = exclusiveGroupInstance.getNodeInstances();
            		stream.writeInt(groupNodeInstances.size());
            		for (NodeInstance groupNodeInstance: groupNodeInstances) {
            			stream.writeLong(groupNodeInstance.getId());
            		}
            	}
            }
        } else if (nodeInstance instanceof ForEachNodeInstance) {
            stream.writeShort(PersisterEnums.FOR_EACH_NODE_INSTANCE);
            ForEachNodeInstance forEachNodeInstance = (ForEachNodeInstance) nodeInstance;
            List<NodeInstance> nodeInstances = new ArrayList<NodeInstance>(forEachNodeInstance.getNodeInstances());
            Collections.sort(nodeInstances,
                    new Comparator<NodeInstance>() {
                        public int compare(NodeInstance o1,
                                NodeInstance o2) {
                            return (int) (o1.getId() - o2.getId());
                        }
                    });
            for (NodeInstance subNodeInstance : nodeInstances) {
                if (subNodeInstance instanceof CompositeContextNodeInstance) {
                    stream.writeShort(PersisterEnums.NODE_INSTANCE);
                    writeNodeInstance(context,
                            subNodeInstance);
                }
            }
            stream.writeShort(PersisterEnums.END);
        } else {
            throw new IllegalArgumentException("Unknown node instance type: " + nodeInstance);
        }
    }
	@Override
	public NodeInstance readNodeInstance(MarshallerReaderContext context,
            NodeInstanceContainer nodeInstanceContainer,
            WorkflowProcessInstance processInstance) throws IOException {
        ObjectInputStream stream = context.stream;
        long id = stream.readLong();
        long nodeId = stream.readLong();
        int nodeType = stream.readShort();
        NodeInstanceImpl nodeInstance = readNodeInstanceContent(nodeType,
                stream, context, processInstance);

        nodeInstance.setNodeId(nodeId);
        nodeInstance.setNodeInstanceContainer(nodeInstanceContainer);
        nodeInstance.setProcessInstance((org.jbpm.workflow.instance.WorkflowProcessInstance) processInstance);
        nodeInstance.setId(id);

        switch (nodeType) {
            case PersisterEnums.COMPOSITE_NODE_INSTANCE:
            case PersisterEnums.DYNAMIC_NODE_INSTANCE:
                int nbVariables = stream.readInt();
                if (nbVariables > 0) {
                    Context variableScope = ((org.jbpm.process.core.Process) ((org.jbpm.process.instance.ProcessInstance)
                		processInstance).getProcess()).getDefaultContext(VariableScope.VARIABLE_SCOPE);
                    VariableScopeInstance variableScopeInstance = (VariableScopeInstance) ((CompositeContextNodeInstance) nodeInstance).getContextInstance(variableScope);
                    for (int i = 0; i < nbVariables; i++) {
                        String name = stream.readUTF();
        				try {
        					int index = stream.readInt();
        					ObjectMarshallingStrategy strategy = context.resolverStrategyFactory
        							.getStrategy(index);

        					Object value = strategy.read(stream);
        					variableScopeInstance.internalSetVariable(name, value);
        				} catch (ClassNotFoundException e) {
        					throw new IllegalArgumentException(
        							"Could not reload variable " + name);
        				}
                    }
                }
                while (stream.readShort() == PersisterEnums.NODE_INSTANCE) {
                    readNodeInstance(context,
                            (CompositeContextNodeInstance) nodeInstance,
                            processInstance);
                }
                
                int exclusiveGroupInstances = stream.readInt();
            	for (int i = 0; i < exclusiveGroupInstances; i++) {
                    ExclusiveGroupInstance exclusiveGroupInstance = new ExclusiveGroupInstance();
                    ((org.jbpm.process.instance.ProcessInstance) processInstance).addContextInstance(ExclusiveGroup.EXCLUSIVE_GROUP, exclusiveGroupInstance);
                    int nodeInstances = stream.readInt();
                    for (int j = 0; j < nodeInstances; j++) {
                        long nodeInstanceId = stream.readLong();
                        NodeInstance groupNodeInstance = processInstance.getNodeInstance(nodeInstanceId);
                        if (groupNodeInstance == null) {
                        	throw new IllegalArgumentException("Could not find node instance when deserializing exclusive group instance: " + nodeInstanceId);
                        }
                        exclusiveGroupInstance.addNodeInstance(groupNodeInstance);
                    }
            	}

                break;
            case PersisterEnums.FOR_EACH_NODE_INSTANCE:
                while (stream.readShort() == PersisterEnums.NODE_INSTANCE) {
                    readNodeInstance(context,
                            (ForEachNodeInstance) nodeInstance,
                            processInstance);
                }
                break;
            default:
            // do nothing
        }

        return nodeInstance;
    }
	@Override
	protected NodeInstanceImpl readNodeInstanceContent(int nodeType, ObjectInputStream stream, MarshallerReaderContext context,
			WorkflowProcessInstance processInstance) throws IOException {
		
		((WorkflowProcessImpl)processInstance.getProcess()).setAutoComplete(true);
		
		NodeInstanceImpl nodeInstance = null;
		switch (nodeType) {
		case PersisterEnums.RULE_SET_NODE_INSTANCE:
			nodeInstance = new RuleSetNodeInstance();
			int nbTimerInstances = stream.readInt();
			if (nbTimerInstances > 0) {
				List<Long> timerInstances = new ArrayList<Long>();
				for (int i = 0; i < nbTimerInstances; i++) {
					timerInstances.add(stream.readLong());
				}
				((RuleSetNodeInstance) nodeInstance).internalSetTimerInstances(timerInstances);
			}
			break;
		case PersisterEnums.HUMAN_TASK_NODE_INSTANCE:
			nodeInstance = new HumanTaskNodeInstance();
			((HumanTaskNodeInstance) nodeInstance).internalSetWorkItemId(stream.readLong());
			nbTimerInstances = stream.readInt();
			if (nbTimerInstances > 0) {
				List<Long> timerInstances = new ArrayList<Long>();
				for (int i = 0; i < nbTimerInstances; i++) {
					timerInstances.add(stream.readLong());
				}
				((HumanTaskNodeInstance) nodeInstance).internalSetTimerInstances(timerInstances);
			}
			break;
		case PersisterEnums.WORK_ITEM_NODE_INSTANCE:
			nodeInstance = new WorkItemNodeInstance();
			((WorkItemNodeInstance) nodeInstance).internalSetWorkItemId(stream.readLong());
			nbTimerInstances = stream.readInt();
			if (nbTimerInstances > 0) {
				List<Long> timerInstances = new ArrayList<Long>();
				for (int i = 0; i < nbTimerInstances; i++) {
					timerInstances.add(stream.readLong());
				}
				((WorkItemNodeInstance) nodeInstance).internalSetTimerInstances(timerInstances);
			}
			break;
		case PersisterEnums.SUB_PROCESS_NODE_INSTANCE:
			nodeInstance = new SubProcessNodeInstance();
			((SubProcessNodeInstance) nodeInstance).internalSetProcessInstanceId(stream.readLong());
			nbTimerInstances = stream.readInt();
			if (nbTimerInstances > 0) {
				List<Long> timerInstances = new ArrayList<Long>();
				for (int i = 0; i < nbTimerInstances; i++) {
					timerInstances.add(stream.readLong());
				}
				((SubProcessNodeInstance) nodeInstance).internalSetTimerInstances(timerInstances);
			}
			break;
		case PersisterEnums.MILESTONE_NODE_INSTANCE:
			nodeInstance = new MilestoneNodeInstance();
			nbTimerInstances = stream.readInt();
			if (nbTimerInstances > 0) {
				List<Long> timerInstances = new ArrayList<Long>();
				for (int i = 0; i < nbTimerInstances; i++) {
					timerInstances.add(stream.readLong());
				}
				((MilestoneNodeInstance) nodeInstance).internalSetTimerInstances(timerInstances);
			}
			break;
		case PersisterEnums.TIMER_NODE_INSTANCE:
			nodeInstance = new TimerNodeInstance();
			((TimerNodeInstance) nodeInstance).internalSetTimerId(stream.readLong());
			break;
		case PersisterEnums.EVENT_NODE_INSTANCE:
			nodeInstance = new EventNodeInstance();
			break;
		case PersisterEnums.JOIN_NODE_INSTANCE:
			nodeInstance = new Uml2JoinInstance();
			int number = stream.readInt();
			if (number > 0) {
				Map<Long, Integer> triggers = new HashMap<Long, Integer>();
				for (int i = 0; i < number; i++) {
					long l = stream.readLong();
					int count = stream.readInt();
					triggers.put(l, count);
				}
				((JoinInstance) nodeInstance).internalSetTriggers(triggers);
			}
			break;
		case PersisterEnums.COMPOSITE_NODE_INSTANCE:
			nodeInstance = new CompositeContextNodeInstance();
			nbTimerInstances = stream.readInt();
			if (nbTimerInstances > 0) {
				List<Long> timerInstances = new ArrayList<Long>();
				for (int i = 0; i < nbTimerInstances; i++) {
					timerInstances.add(stream.readLong());
				}
				((CompositeContextNodeInstance) nodeInstance).internalSetTimerInstances(timerInstances);
			}
			break;
		case PersisterEnums.FOR_EACH_NODE_INSTANCE:
			nodeInstance = new ForEachNodeInstance();
			break;
		case PersisterEnums.DYNAMIC_NODE_INSTANCE:
			nodeInstance = new DynamicNodeInstance();
			nbTimerInstances = stream.readInt();
			if (nbTimerInstances > 0) {
				List<Long> timerInstances = new ArrayList<Long>();
				for (int i = 0; i < nbTimerInstances; i++) {
					timerInstances.add(stream.readLong());
				}
				((CompositeContextNodeInstance) nodeInstance).internalSetTimerInstances(timerInstances);
			}
			break;
		case PersisterEnums.STATE_NODE_INSTANCE:
			nodeInstance = new Uml2StateInstance();
			nbTimerInstances = stream.readInt();
			if (nbTimerInstances > 0) {
				List<Long> timerInstances = new ArrayList<Long>();
				for (int i = 0; i < nbTimerInstances; i++) {
					timerInstances.add(stream.readLong());
				}
				((CompositeContextNodeInstance) nodeInstance).internalSetTimerInstances(timerInstances);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown node type: " + nodeType);
		}
		return nodeInstance;
	}
}