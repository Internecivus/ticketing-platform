import React from "react";
import Row from "./Row";

class Body extends React.Component {
  rows() {
    const { data, events } = this.props;
    return data.map((row) => <Row key={row.id} data={row} events={events} />);
  }

  render() {
    return <tbody>{this.rows()}</tbody>;
  }
}

export default Body;
