import useTranslation from "next-translate/useTranslation";
import withTranslation from "next-translate/withTranslation";
import React from "react";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import PropTypes from "prop-types";

function ConfirmModal(props) {
  const { t } = useTranslation("common");
  const { show, onClose, onYes } = props;

  return (
    <Modal
      backdropClassName="modal-embedded-backdrop"
      className="modal-embedded"
      show={show}
      size="sm"
      onHide={onClose}
    >
      <Modal.Body>{t("message.confirmDelete.confirmation")}</Modal.Body>
      <Modal.Footer>
        <Button variant="danger" onClick={onYes}>
          {t("yes")}
        </Button>
        <Button variant="secondary" onClick={onClose}>
          {t("no")}
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

ConfirmModal.propTypes = {
  show: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onYes: PropTypes.func.isRequired,
};

export default ConfirmModal;
