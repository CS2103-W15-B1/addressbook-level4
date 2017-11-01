package seedu.address.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.Remark;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.tag.Tag;

/**
 * Parses a .vcf file into a list of {@code ReadOnlyPerson}.
 */
public class VcfImport {
    public static List<ReadOnlyPerson> getPersonList(File file) throws IOException, IllegalValueException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)));
        String newLine = br.readLine();
        List<ReadOnlyPerson> importList = new ArrayList<>();
        while (newLine != null) {
            if (newLine.equals("BEGIN:VCARD")) {
                Name name = null;
                Email email = null;
                Phone phone = null;
                Address address = null;
                Remark remark = new Remark("");
                Set<Tag> tagList = new HashSet<>();

                newLine = br.readLine();
                while (!newLine.equals("END:VCARD")) {
                    String type = newLine.split(":")[0];
                    String parameter = newLine.split(":")[1];

                    switch (type) {
                    case "FN":
                        name = new Name(parameter);
                        break;

                    case "EMAIL":
                        email = new Email(parameter);
                        break;

                    case "TEL":
                        phone = new Phone(parameter);
                        break;

                    case "ADR":
                        address = new Address(parameter);
                        break;
                    case "RM":
                        remark = new Remark(parameter);
                        break;
                    case "TAG":
                        Tag tag = new Tag(parameter);
                        tagList.add(tag);
                        break;
                    default:
                    }
                    newLine = br.readLine();
                }
                newLine = br.readLine();
                ReadOnlyPerson toAdd = new Person(name, phone, email, address, remark, tagList);
                importList.add(toAdd);
            } else {
                newLine = br.readLine();
            }
        }

        return importList;
    }
}