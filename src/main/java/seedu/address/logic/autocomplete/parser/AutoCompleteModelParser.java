package seedu.address.logic.autocomplete.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.logic.autocomplete.AutoCompleteUtils;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;

/** Parses the possible names that the user might have been trying to type,
 *  based on the names currently present in the address book. */
public class AutoCompleteModelParser implements AutoCompleteParser {

    private final Model model;
    private Prefix currentPrefix;
    private List<String> allPossibleMatches = Collections.emptyList();

    public AutoCompleteModelParser(Model model) {
        this.model = model;
    }

    @Override
    public List<String> parseForPossibilities(String stub) {
        final LinkedList<String> possibleMatches = new LinkedList<String>();

        int prefixPosition = AutoCompleteUtils.findPrefixPosition(stub, currentPrefix.toString());
        String staticSection = stub.substring(0, prefixPosition);
        String autoCompleteSection = stub.substring(prefixPosition, stub.length());

        possibleMatches.addAll(allPossibleMatches.stream()
                .filter(possibleMatch -> AutoCompleteUtils.startWithSameLetters(autoCompleteSection, possibleMatch))
                .map(filteredMatch -> staticSection + filteredMatch)
                .collect(Collectors.toList()));
        possibleMatches.add(stub);

        return possibleMatches;
    }

    /**
     * Changes which attribute to match in the Model component, based on the prefix specified,
     * and update possible matches accordingly.
     * @param newPrefix new prefix to parse for input
     */
    public void setPrefix(Prefix newPrefix) {
        currentPrefix = newPrefix;
        updateAllPossibleMatches();
    }

    /**
     * Updates the possible matches list according to currently set prefix.
     */
    private void updateAllPossibleMatches() {
        if (currentPrefix.equals(PREFIX_NAME)) {
            allPossibleMatches = model.getAllNamesInAddressBook();
        } else if (currentPrefix.equals(PREFIX_PHONE)) {
            allPossibleMatches = model.getAllPhonesInAddressBook();
        } else if (currentPrefix.equals(PREFIX_EMAIL)) {
            allPossibleMatches = model.getAllEmailsInAddressBook();
        } else if (currentPrefix.equals(PREFIX_ADDRESS)) {
            allPossibleMatches = model.getAllAddressesInAddressBook();
        } else if (currentPrefix.equals(PREFIX_TAG)) {
            allPossibleMatches = model.getAllTagsInAddressBook();
        } else if (currentPrefix.equals(PREFIX_REMARK)) {
            allPossibleMatches = model.getAllRemarksInAddressBook();
        } else {
            allPossibleMatches = Collections.emptyList();
        }
    }

}
