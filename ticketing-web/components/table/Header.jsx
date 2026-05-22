import withTranslation from "next-translate/withTranslation";
import React from "react";
import HeaderRow from "./HeaderRow";

class Header extends React.Component {
  headers() {
    const { columns } = this.props;
    return columns.map((header) => <HeaderRow key={header.field} data={header} />);
  }

  render() {
    return (
      <thead className="thead-dark">
        <tr>{this.headers()}</tr>
      </thead>
    );
  }
}

export default withTranslation(Header);
