package org.nakeduml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.List;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;


public abstract class MergeNodeObjectTokenUnknownWithInControlToken extends MergeNode<Token, ObjectToken<?>> {

	public MergeNodeObjectTokenUnknownWithInControlToken() {
		super();
	}

	public MergeNodeObjectTokenUnknownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}

	public MergeNodeObjectTokenUnknownWithInControlToken(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract ObjectFlowUnknown getOutFlow();
	
	@Override
	protected abstract List<ObjectFlowUnknown> getInFlows();

	@Override
	protected List<ObjectFlowUnknown> getOutFlows() {
		List<ObjectFlowUnknown> result = new ArrayList<ObjectFlowUnknown>();
		result.add(getOutFlow());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Token> getInTokens() {
		List<Token> result = new ArrayList<Token>();
		for (ActivityEdge<? extends Token> flow : getInFlows()) {
			Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + flow.getName());
			for (Edge edge : iter) {
				Token token;
				if (flow instanceof ControlFlow) {
					token = new ControlToken(edge.getInVertex());
				} else {
					token = new ObjectToken(edge.getInVertex());
				}
				result.add(token);
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Token> getInTokens(String inFlowName) {
		List<Token> result = new ArrayList<Token>();
		for (ActivityEdge<? extends Token> flow : getInFlows()) {
			if (flow.getName().equals(inFlowName)) {
				Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + flow.getName());
				for (Edge edge : iter) {
					Token token;
					if (flow instanceof ControlFlow) {
						token = new ControlToken(edge.getInVertex());
					} else {
						token = new ObjectToken(edge.getInVertex());
					}
					result.add(token);
				}
			}
		}
		return result;
	}

	@Override
	public List<ObjectToken<?>> getOutTokens() {
		List<ObjectToken<?>> result = new ArrayList<ObjectToken<?>>();
		for (ObjectFlowUnknown flow : getOutFlows()) {
			Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + flow.getName());
			for (Edge edge : iter) {
				@SuppressWarnings("rawtypes")
				ObjectToken e = new ObjectToken(edge.getInVertex());
				result.add(e);
			}
		}
		return result;
	}

	@Override
	public List<ObjectToken<?>> getOutTokens(String outFlowName) {
		List<ObjectToken<?>> result = new ArrayList<ObjectToken<?>>();
		for (ObjectFlowUnknown flow : getOutFlows()) {
			if (flow.getName().equals(outFlowName)) {
				Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + flow.getName());
				for (Edge edge : iter) {
					@SuppressWarnings("rawtypes")
					ObjectToken e = new ObjectToken(edge.getInVertex());
					result.add(e);
				}
			}
		}
		return result;
	}
	

}