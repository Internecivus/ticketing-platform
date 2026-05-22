import Button from "react-bootstrap/Button";
import Spinner from "react-bootstrap/Spinner";
import React from "react";
import PropTypes from "prop-types";

function SpinnerButton(props) {
  const { isSpinning, spinningLabel, label, ...other } = props;

  return (
    <Button {...other}>
      {isSpinning && (
        <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" />
      )}
      {isSpinning ? <span className="ms-1">{spinningLabel}</span> : label}
    </Button>
  );
}

SpinnerButton.propTypes = {
  isSpinning: PropTypes.bool.isRequired,
  spinningLabel: PropTypes.string.isRequired,
  label: PropTypes.string.isRequired,
};

export default SpinnerButton;
