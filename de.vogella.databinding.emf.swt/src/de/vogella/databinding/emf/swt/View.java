package de.vogella.databinding.emf.swt;

import model.ModelFactory;
import model.ModelPackage;
import model.Person;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class View extends ViewPart {
	public static final String ID = "de.vogella.databinding.emf.swt.view";

	private TableViewer viewer;
	private WritableList input;

	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof Object[]) {
				return (Object[]) parent;
			}
			return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		// put things to order
		parent.setLayout(new GridLayout(1, false));
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;

		// define the viewer
		viewer = new TableViewer(parent);
		viewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setWidth(100);
		column.getColumn().setText("First Name");
		column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setWidth(100);
		column.getColumn().setText("Last name");
		viewer.getTable().setHeaderVisible(true);

		// bind values
		input = new WritableList(MyModel.getPersons(), Person.class);
		EStructuralFeature[] features = {
				ModelPackage.Literals.PERSON__FIRST_NAME,
				ModelPackage.Literals.PERSON__LAST_NAME };
		ViewerSupport.bind(viewer, input, EMFProperties.values(features));

		Button delete = new Button(parent, SWT.PUSH);
		delete.setText("Delete");
		delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!viewer.getSelection().isEmpty()) {
					IStructuredSelection selection = (IStructuredSelection) viewer
							.getSelection();
					Person p = (Person) selection.getFirstElement();
					input.remove(p);
				}
			}
		});
		
		Button add = new Button(parent, SWT.PUSH);
		add.setText("Add");
		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// initialize the model
				ModelPackage.eINSTANCE.eClass();

				// retrieve the default factory singleton
				ModelFactory factory = ModelFactory.eINSTANCE;
				Person p = factory.createPerson();
				p.setFirstName("Test1");
				p.setLastName("Test2");
				input.add(p);
			}
		});

		Button change = new Button(parent, SWT.PUSH);
		change.setText("Switch First / Lastname");
		change.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!viewer.getSelection().isEmpty()) {
					IStructuredSelection selection = (IStructuredSelection) viewer
							.getSelection();
					Person p = (Person) selection.getFirstElement();
					String temp = p.getLastName();
					p.setLastName(p.getFirstName());
					p.setFirstName(temp);
				}
			}
		});

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}