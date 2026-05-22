import withTranslation from "next-translate/withTranslation";
import React from "react";
import BootstrapTable from "react-bootstrap/Table";
import Header from "./Header";
import Body from "./Body";
import Spinner from "react-bootstrap/Spinner";

class Table extends React.Component {
  noDataPlaceholder() {
    const { t } = this.props.i18n;
    const { noDataPlaceholder, noDataPlaceholderText } = this.props;
    if (noDataPlaceholder != null) {
      return noDataPlaceholder;
    }
    this.noDataPlaceholderText = noDataPlaceholderText || t("common:dataTable.noData");
    return <div className="ms-3 text-muted">{this.noDataPlaceholderText}</div>;
  }

  transformData() {
    const { data, columns } = this.props;
    return data.map((object) => {
      const transformedObject = {
        id: object.id,
        fields: {},
      };
      columns.forEach((column) => {
        transformedObject.fields[column.field] = object[column.field];
      });
      return transformedObject;
    });
  }

  isThereData() {
    const { data } = this.props;
    return Array.isArray(data) && data.length;
  }

  loader() {
    return (
      <div className="data-table-loader-overlay">
        <div className="data-table-loader">
          <Spinner animation="border" />
        </div>
      </div>
    );
  }

  render() {
    const { columns, events, size, isLoading } = this.props;

    if (!this.isThereData()) {
      return this.noDataPlaceholder();
    }
    return (
      <div className="data-table">
        {isLoading ? this.loader() : null}
        <BootstrapTable bordered hover size={size}>
          <Header columns={columns} />
          <Body data={this.transformData()} events={events || {}} />
        </BootstrapTable>
      </div>
    );
  }
}

export default withTranslation(Table);
