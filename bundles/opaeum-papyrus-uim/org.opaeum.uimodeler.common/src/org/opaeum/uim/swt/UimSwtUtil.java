package org.opaeum.uim.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.opaeum.uim.action.ActionKind;
import org.opaeum.uim.action.BuiltInLinkKind;
import org.opaeum.uim.control.ControlKind;
import org.opaeum.uim.panel.Orientation;

public class UimSwtUtil{
	public static final String WBP_NEED_IMAGE = "WBP_NEED_IMAGE";
	public static Image getImageFor(ActionKind kind){
			switch(kind){
			case ADD:
				return UimRuntimeActivator.getDefault().getImage("/icons/add.ico");
			case DELETE:
				return UimRuntimeActivator.getDefault().getImage("/icons/delete.ico");
			case REFRESH:
				return UimRuntimeActivator.getDefault().getImage("/icons/refresh.ico");
			case UPDATE:
				return UimRuntimeActivator.getDefault().getImage("/icons/save.ico");
			default:
				return UimRuntimeActivator.getDefault().getImage("/icons/add.ico");
			}
	}
	public static Image getImageFor(BuiltInLinkKind kind){
			switch(kind){
			case AUDIT_TRAIL:
				return UimRuntimeActivator.getDefault().getImage("/icons/audit-trail.ico");
			case BUSINESS_INTELLIGENCE:
				return UimRuntimeActivator.getDefault().getImage("/icons/business-intelligence.ico")
						;
			case EDIT:
				return UimRuntimeActivator.getDefault().getImage("/icons/edit.ico");
			case VIEW:
				return UimRuntimeActivator.getDefault().getImage("/icons/view.ico");
			default:
				return UimRuntimeActivator.getDefault().getImage("/icons/add.ico");
			}
	}
	public static void populateControl(IUimFieldComposite fig,ControlKind kind,Orientation orientation){
		if(fig.getControl() != null && !fig.getControl().isDisposed()){
			fig.getControl().dispose();
		}
		switch(kind){
		case CHECK_BOX:
			fig.setControl(new Button((Composite) fig, SWT.CHECK));
			break;
		case DATE_POPUP:
			fig.setControl(new DateTime((Composite) fig, SWT.BORDER | SWT.CALENDAR | SWT.DROP_DOWN | SWT.DATE));
			break;
		case DATE_TIME_POPUP:
			fig.setControl(new DateTime((Composite) fig, SWT.BORDER | SWT.CALENDAR | SWT.DROP_DOWN | SWT.TIME | SWT.DATE));
			break;
		case DATE_SCROLLER:
			fig.setControl(new DateTime((Composite) fig, SWT.BORDER));
			break;
		case DROPDOWN:
			CCombo c = new CCombo((Composite) fig, SWT.BORDER | SWT.READ_ONLY);
			c.add("Item 1");
			c.add("Item 2");
			c.add("Item 3");
			c.add("Item 4");
			fig.setControl(c);
			break;
		case LIST_BOX:
			List list = new List((Composite) fig, SWT.BORDER | SWT.MULTI);
			list.add("Item 1");
			list.add("Item 2");
			list.add("Item 3");
			list.add("Item 4");
			fig.setControl(list);
			break;
		case TREE_VIEW:
			Tree tree = new Tree((Composite) fig, SWT.BORDER | SWT.MULTI);
			TreeItem root = new TreeItem(tree, SWT.NONE);
			root.setText("Root");
			root.setExpanded(true);
			TreeItem _1_1 = new TreeItem(root, SWT.NONE);
			_1_1.setText("Item 1.1");
			_1_1.setExpanded(true);
			TreeItem _1_2 = new TreeItem(root, SWT.NONE);
			_1_2.setText("Item 1.2");
			_1_2.setExpanded(true);
			TreeItem _1_2_1 = new TreeItem(_1_2, SWT.NONE);
			_1_2_1.setText("Item 1.2.1");
			_1_2_1.setExpanded(true);
			TreeItem _1_2_2 = new TreeItem(_1_2, SWT.NONE);
			_1_2_2.setText("Item 1.2.2");
			_1_2_2.setExpanded(true);
			tree.setTopItem(root);
			tree.showItem(_1_2_2);
			fig.setControl(tree);
			break;
		case POPUP_SEARCH:
			fig.setControl(new CSingleObjectChooser((Composite) fig, SWT.BORDER));
			break;
		case NUMBER_SCROLLER:
			// fig.setControl(new NumberScroller((Composite) fig, SWT.NONE | SWT.BORDER));
			fig.setControl(new Spinner((Composite) fig, SWT.NONE | SWT.BORDER));
			break;
		case TEXT:
			fig.setControl(new Text((Composite) fig, SWT.NONE | SWT.BORDER));
			break;
		case TEXT_AREA:
			fig.setControl(new Text((Composite) fig, SWT.BORDER | SWT.MULTI));
			break;
		case RADIO_BUTTON:
			Composite radioGroup = new Composite((Composite) fig, SWT.BORDER){
				{
					setLayout(new GridLayout(3, true));
					new Button(this, SWT.RADIO).setText("Option 1");
					new Button(this, SWT.RADIO).setText("Option 2");
					new Button(this, SWT.RADIO).setText("Option 3");
				}
				public void setVertical(boolean v){
					if(v){
						setLayout(new GridLayout(1, true));
					}else{
						setLayout(new GridLayout(3, true));
					}
					layout();
				}
			};
			fig.setControl(radioGroup);
			radioGroup.layout();
			break;
		case LINK:
			if(orientation == Orientation.VERTICAL){
				fig.setControl(new LinkComposite((Composite) fig, SWT.BORDER, true));
			}else{
				fig.setControl(new LinkComposite((Composite) fig, SWT.BORDER));
			}
			break;
		case LABEL:
			CLabel label = new CLabel((Composite) fig, SWT.BORDER);
			label.setText("<Object Name>");
			fig.setControl(label);
			break;
		case TOGGLE_BUTTON:
			fig.setControl(new Button((Composite) fig, SWT.TOGGLE));
			break;
		case SELECTION_TABLE:
			fig.setControl(new Button((Composite) fig, SWT.TOGGLE));
			break;
		}
	}
	public static void setOrientation(Orientation or,IUimFieldComposite fig,Integer minimumLabelWidth){
		GridLayout layout;
		if(or == Orientation.VERTICAL){
			layout = new GridLayout(1, false);
			fig.setLayout(layout);
			fig.getLabel().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
			fig.getControl().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		}else{
			layout = new GridLayout(2, false);
			fig.setLayout(layout);
			fig.setMinimumLabelWidth(minimumLabelWidth);
			((GridData) fig.getLabel().getLayoutData()).grabExcessHorizontalSpace = false;
			fig.getControl().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		}
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		fig.layout();
	}
}
