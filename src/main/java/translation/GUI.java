package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JPanel countryPanel = new JPanel();
            countryPanel.add(new JLabel("Country:"));
            Translator translator = new JSONTranslator();

            CountryCodeConverter converter = new CountryCodeConverter();
            List<String> countryCodeList = translator.getCountryCodes();
            List<String> countryList = new ArrayList<>();
            for (String code: countryCodeList) {
                String country = converter.fromCountryCode(code);
                countryList.add(country);
            }

            JComboBox<String> dropDown = new JComboBox<>(countryList.toArray(new String[0]));
            countryPanel.add(dropDown);

            List<String> langCodeList = translator.getLanguageCodes();
            List<String> langList = new ArrayList<>();
            LanguageCodeConverter langConverter = new LanguageCodeConverter();
            for (String code: langCodeList) {
                String language = langConverter.fromLanguageCode(code);
                langList.add(language);
            }

            JList<String> languageList = new JList<>(langList.toArray(new String[0]));
            JScrollPane languageScrollPane = new JScrollPane(languageList);
            languageScrollPane.setPreferredSize(new java.awt.Dimension(500, 200));


            JPanel languagePanel = new JPanel();
            languagePanel.add(languageScrollPane, java.awt.BorderLayout.CENTER);

            JPanel translationPanel = new JPanel();
            JLabel resultLabelText = new JLabel("Translation:");
            translationPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            translationPanel.add(resultLabel);

            ActionListener updateTranslation = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateTranslation(translator, converter, langConverter, dropDown, languageList, resultLabel);
                }
            };

            ListSelectionListener listListener = new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) { // Only trigger when selection is final
                        updateTranslation(translator, converter, langConverter, dropDown, languageList, resultLabel);
                    }
                }
            };

            dropDown.addActionListener(updateTranslation);
            languageList.addListSelectionListener(listListener);

            updateTranslation(translator, converter, langConverter, dropDown, languageList, resultLabel);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(countryPanel);
            mainPanel.add(translationPanel);
            mainPanel.add(languagePanel);


            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);




        });
    }    private static void updateTranslation(Translator translator, CountryCodeConverter countryConverter,
                                               LanguageCodeConverter langConverter, JComboBox<String> countryDropdown,
                                               JList<String> languageList, JLabel resultLabel) {
        String countryName = (String) countryDropdown.getSelectedItem();
        String languageName = languageList.getSelectedValue();

        if (countryName != null && languageName != null) {

            String countryCode = countryConverter.fromCountry(countryName).toLowerCase();
            String languageCode = langConverter.fromLanguage(languageName);

            String result = translator.translate(countryCode, languageCode);
            if (result == null) {
                result = "no translation found!";
            }
            resultLabel.setText(result);
        }
    }
}
