import withTranslation from "next-translate/withTranslation";
import React from "react";

class HeaderRow extends React.Component {
  render() {
    const { data } = this.props;
    const { t } = this.props.i18n;
    return <th key={data.field}>{t("table:" + data.field)}</th>;
  }
}

export default withTranslation(HeaderRow);
