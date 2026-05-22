import useTranslation from "next-translate/useTranslation";
import React from "react";
import { Row } from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";

function AuditedEntityMetadata(props) {
  const { entityData } = props;
  const { t } = useTranslation("common");

  const updatedValue = () =>
    entityData.lastUpdateUser || entityData.lastUpdatedDate
      ? `${entityData.lastUpdateUser} - ${entityData.lastUpdatedDate}`
      : "";

  const createdValue = () =>
    entityData.createdUser || entityData.createdDate
      ? `${entityData.createdUser} - ${entityData.createdDate}`
      : "";

  return (
    <fieldset disabled>
      <Row className="mt-3">
        <Form.Group as={Col}>
          <Form.Label column="md">{t("common:created")}</Form.Label>
          <Form.Control
            size={"sm"}
            onChange={() => null}
            value={createdValue()}
            name="audited-entity-metadata"
          />
        </Form.Group>

        <Form.Group as={Col}>
          <Form.Label column="md">{t("common:updated")}</Form.Label>
          <Form.Control
            size={"sm"}
            onChange={() => null}
            value={updatedValue()}
            name="audited-entity-metadata"
          />
        </Form.Group>
      </Row>
    </fieldset>
  );
}

export default AuditedEntityMetadata;
