import React from 'react';
import BsAlert from 'react-bootstrap/Alert';
import PropTypes from 'prop-types';

function Alert(props) {
  const { show, data, onClose } = props;

  return show && (
    <BsAlert
      variant={data.variant}
      onClose={onClose}
      dismissible
    >
      {data.title && <BsAlert.Heading>{data.title}</BsAlert.Heading>}
      <div>{data.message}</div>
    </BsAlert>
  );
}

Alert.propTypes = {
  show: PropTypes.bool.isRequired,
  data: PropTypes.exact({
    variant: PropTypes.oneOf(['danger', 'info', 'warning', 'success']),
    title: PropTypes.string,
    message: PropTypes.string,
  }).isRequired,
};

export default Alert;
