import Spinner from "react-bootstrap/Spinner";
import React from "react";
import PropTypes from "prop-types";

function LoadingModal(props) {
  const { size } = props;
  return (
    <div className={`modal-loading modal-loading--${size}`}>
      <Spinner animation="border" />
    </div>
  );
}

LoadingModal.propTypes = {
  size: PropTypes.oneOf(["xs", "sm", "md", "lg", "xl"]),
};

LoadingModal.defaultProps = {
  size: "md",
};

export default LoadingModal;
