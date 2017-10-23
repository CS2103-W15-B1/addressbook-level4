package seedu.address.logic.autocomplete.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
//import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
//import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;

import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

public class AutoCompleteModelParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AutoCompleteModelParser parser;
    private ModelStubWithRequiredMethods mockModel;

    @Before
    public void fillMockModel() {
        mockModel = new ModelStubWithRequiredMethods();
        try {
            mockModel.addAllPersons(Arrays.asList(
                    new ReadOnlyPerson[]{ALICE, AMY, BENSON, BOB, CARL, DANIEL, ELLE, FIONA, GEORGE}));
        } catch (DuplicatePersonException ex) {
            fail("This exception should not be thrown.");
        }
        parser = new AutoCompleteModelParser(mockModel);
    }

    @Test
    public void testParseName() {
        parser.setPrefix(PREFIX_NAME);
        //multiple possibilities matched
        assertEquals(parser.parseForPossibilities("add n/a"),
                Arrays.asList(new String[] {"add n/" + ALICE.getName().toString(),
                                            "add n/" + AMY.getName().toString(),
                                            "add n/a"}));

        //single possibility matched
        assertEquals(parser.parseForPossibilities("add n/f"),
                Arrays.asList(new String[] {"add n/" + FIONA.getName().toString(),
                                            "add n/f"}));

        //no possibility matched
        assertEquals(parser.parseForPossibilities("add n/r"),
                Arrays.asList(new String[] {"add n/r"}));
    }

    @Test
    public void testParsePhone() {
        parser.setPrefix(PREFIX_PHONE);
        //multiple possibilities matched
        assertEquals(parser.parseForPossibilities("edit 1 p/8"),
                Arrays.asList(new String[] {"edit 1 p/" + ALICE.getPhone().toString(),
                                            "edit 1 p/" + DANIEL.getPhone().toString(),
                                            "edit 1 p/8"}));

        //single possibility matched
        assertEquals(parser.parseForPossibilities("add p/111"),
                Arrays.asList(new String[] {"add p/" + AMY.getPhone().toString(),
                                            "add p/111"}));

        //no possibility matched, even if some phone numbers contain the sequence
        assertEquals(parser.parseForPossibilities("add p/482"),
                Arrays.asList(new String[] {"add p/482"}));
    }

    @Test
    public void testParseEmail() {
        parser.setPrefix(PREFIX_EMAIL);
        //multiple possibilities matched
        assertEquals(parser.parseForPossibilities("edit 5 e/a"),
                Arrays.asList(new String[] {"edit 5 e/" + ALICE.getEmail().toString(),
                                            "edit 5 e/" + AMY.getEmail().toString(),
                                            "edit 5 e/" + GEORGE.getEmail().toString(),
                                            "edit 5 e/a"}));

        //single possibility matched
        assertEquals(parser.parseForPossibilities("edit 12 e/corn"),
                Arrays.asList(new String[] {"edit 12 e/" + DANIEL.getEmail().toString(),
                        "edit 12 e/corn"}));

        //no possibility matched
        assertEquals(parser.parseForPossibilities("add e/example.com"),
                Arrays.asList(new String[] {"add e/example.com"}));
    }

    @Test
    public void testParseAddress() {
        parser.setPrefix(PREFIX_ADDRESS);
        //multiple possibilities matched
        assertEquals(parser.parseForPossibilities("add a/1"),
                Arrays.asList(new String[] {"add a/" + ALICE.getAddress().toString(),
                        "add a/" + DANIEL.getAddress().toString(),
                        "add a/1"}));

        //single possibility matched
        assertEquals(parser.parseForPossibilities("edit 2 a/10"),
                Arrays.asList(new String[] {"edit 2 a/" + DANIEL.getAddress().toString(),
                        "edit 2 a/10"}));

        //no possibility matched
        assertEquals(parser.parseForPossibilities("add a/serangoon"),
                Arrays.asList(new String[] {"add a/serangoon"}));
    }

    @After
    public void cleanUpMockModel() {
        mockModel = null;
        parser = null;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(ReadOnlyPerson person) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public List<String> getAllNamesInAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public List<String> getAllPhonesInAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public List<String> getAllEmailsInAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public List<String> getAllAddressesInAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public List<String> getAllTagsInAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public List<String> getAllRemarksInAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson(ReadOnlyPerson target) throws PersonNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson)
                throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<ReadOnlyPerson> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void removeTag(Tag tag) {
            fail("This method should not be called");
        }

        @Override
        public void removeTag(Index index, Tag tag) {
            fail("This method should not be called");
        }
    }

    /**
     * A Model stub that allows calling of certain methods used in the test,
     * including some required accessors and addPerson
     */
    private class ModelStubWithRequiredMethods extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public void addPerson(ReadOnlyPerson person) throws DuplicatePersonException {
            personsAdded.add(new Person(person));
        }

        public void addAllPersons(List<ReadOnlyPerson> persons) throws DuplicatePersonException {
            for (ReadOnlyPerson person : persons) {
                this.addPerson(person);
            }
        }

        @Override
        public List<String> getAllNamesInAddressBook() {
            return personsAdded.stream()
                    .map(person -> person.getName().toString())
                    .collect(Collectors.toList());
        }

        @Override
        public List<String> getAllPhonesInAddressBook() {
            return personsAdded.stream()
                    .map(person -> person.getPhone().toString())
                    .collect(Collectors.toList());
        }

        @Override
        public List<String> getAllEmailsInAddressBook() {
            return personsAdded.stream()
                    .map(person -> person.getEmail().toString())
                    .collect(Collectors.toList());
        }

        @Override
        public List<String> getAllAddressesInAddressBook() {
            return personsAdded.stream()
                    .map(person -> person.getAddress().toString())
                    .collect(Collectors.toList());
        }

        @Override
        public List<String> getAllTagsInAddressBook() {
            return personsAdded.stream()
                    .map(tag -> tag.toString().substring(1, tag.toString().length() - 1))
                    .collect(Collectors.toList());
        }

        @Override
        public List<String> getAllRemarksInAddressBook() {
            return personsAdded.stream()
                    .map(person -> person.getRemark().toString())
                    .collect(Collectors.toList());
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
