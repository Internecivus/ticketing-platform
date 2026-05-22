import withTranslation from "next-translate/withTranslation";
import React from "react";
import Form from "react-bootstrap/Form";
import Application from "../app/application";
import setLanguage from "next-translate/setLanguage";

class LanguageSelect extends React.Component {
  async changeLanguage(event) {
    await setLanguage(event.target.value);
  }

  languages() {
    const { t } = this.props.i18n;
    return Application.availableLanguages.map((language) => (
      <option value={language} key={language}>
        {t(`common:language.${language}`)}
      </option>
    ));
  }

  render() {
    const { lang } = this.props.i18n;
    return (
      <Form.Select as="select" onChange={this.changeLanguage} defaultValue={lang}>
        {this.languages()}
      </Form.Select>
    );
  }
}

export default withTranslation(LanguageSelect);
