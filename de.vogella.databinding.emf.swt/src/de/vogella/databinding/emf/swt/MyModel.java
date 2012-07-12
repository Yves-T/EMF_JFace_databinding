package de.vogella.databinding.emf.swt;

import java.util.ArrayList;
import java.util.List;

import model.ModelFactory;
import model.ModelPackage;
import model.Person;

public class MyModel {
	public static List<Person> getPersons() {
		// initialize the model
		ModelPackage.eINSTANCE.eClass();

		// retrieve the default factory singleton
		ModelFactory factory = ModelFactory.eINSTANCE;

		List<Person> persons = new ArrayList<Person>();

		Person person = factory.createPerson();
		person.setFirstName("John");
		person.setLastName("Doe");
		persons.add(person);
		person = factory.createPerson();
		person.setFirstName("Joe");
		person.setLastName("Darcy");
		persons.add(person);
		person = factory.createPerson();
		person.setFirstName("Jim");
		person.setLastName("Knopf");
		persons.add(person);

		return persons;
	}
}
