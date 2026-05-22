import withTranslation from "next-translate/withTranslation";
import React from "react";
import Icon from "../Icon";

class Row extends React.Component {
  constructor(props) {
    super(props);
    this.onClick = this.onClick.bind(this);
  }

  onClick(event) {
    const { data, events } = this.props;
    if (events.onClick) events.onClick(data.id, event);
  }

  deleteIcon() {
    const { events, data } = this.props;
    const { t } = this.props.i18n;
    return (
      <Icon
        containerClassName="float-end icon-container-table"
        buttonVariant="danger"
        size="xs"
        tooltip={t("common:actions.delete")}
        variant={Icon.variant.DELETE}
        onClick={() => events.onDelete(data.id)}
      />
    );
  }

  values() {
    const { data, events } = this.props;
    return Object.entries(data.fields).map(([key, value], i, array) => {
      if (events.onDelete && array.length - 1 === i) {
        return (
          <td key={key}>
            {value}
            {this.deleteIcon()}
          </td>
        );
      }
      return <td key={key}>{value}</td>;
    });
  }

  render() {
    const { events } = this.props;
    return (
      <tr onClick={this.onClick} className={events.onClick ? "table-row-clickable" : ""}>
        {this.values()}
      </tr>
    );
  }
}

export default withTranslation(Row);
