import withTranslation from "next-translate/withTranslation";
import Form from "react-bootstrap/Form";
import React from "react";
import PropTypes from "prop-types";

class MultipleCheckboxList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      values: props.value,
    };
  }

  static getDerivedStateFromProps(props) {
    return {
      values: props.value,
    };
  }

  handleCheckboxChange(event, eventItem) {
    const { onChange } = this.props;
    const { values } = this.state;
    const target = event.currentTarget;
    let newValues = [];

    if (target.checked) {
      newValues = values.concat(eventItem);
    } else {
      values.splice(
        values.findIndex((item) => item.id === eventItem.id),
        1
      );
      newValues = values;
    }
    this.setState({ values: newValues });
    return onChange(newValues);
  }

  checkboxes() {
    const { list, labels } = this.props;
    const { t } = this.props.i18n;
    const { values } = this.state;

    return list.map((item) => (
      <Form.Check
        id={`group-${item.name}`}
        key={item.id}
        label={t(`common:${labels}.${item.name}`)}
        value={item.id}
        checked={values.find((value) => value.id === item.id) !== undefined}
        onChange={(event) => this.handleCheckboxChange(event, item)}
      />
    ));
  }

  render() {
    const { isInvalid } = this.props;
    return (
      <Form.Control isInvalid={isInvalid} as="div" className="multiple-checkbox">
        {this.checkboxes()}
      </Form.Control>
    );
  }
}

MultipleCheckboxList.propTypes = {
  labels: PropTypes.string.isRequired,
  list: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.string.isRequired,
      id: PropTypes.number.isRequired,
    })
  ).isRequired,
  value: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.string.isRequired,
      id: PropTypes.number.isRequired,
    })
  ).isRequired,
  onChange: PropTypes.func.isRequired,
  isInvalid: PropTypes.bool,
};

MultipleCheckboxList.defaultProps = {
  isInvalid: false,
};

export default withTranslation(MultipleCheckboxList);
