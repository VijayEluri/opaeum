package org.opaeum.uimodeler.modelexplorer.handler;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.opaeum.uim.cube.CubeFactory;
import org.opaeum.uim.cube.CubePackage;
import org.opaeum.uim.cube.CubeQuery;

public class CreateCubeQueryCommandHandler extends CreateCommandHandler{
	@Override
	protected EReference getFeature(){
		return CubePackage.eINSTANCE.getCubeQueryEditor_Queries();
	}
	@Override
	protected EObject getNewObject(){
		CubeQuery p = CubeFactory.eINSTANCE.createCubeQuery();
		p.setName("NewCubeQuery");
		return p;
	}
}