import withTranslation from "next-translate/withTranslation";
import React from "react";
import PropTypes from "prop-types";
import { Button, Form } from "react-bootstrap";
import AuditedEntityMetadata from "../AuditedEntityMetadata";
import AssignUserModal from "./AssignUserModal";
import Tables from "../../app/tables";
import EditModal from "../modal/EditModal";
import { CategoriesAPI } from "../../app/api";
import DataTable from "../table/DataTable";
import Forms from "../../app/forms";

class CategoryEditModal extends React.Component {
  static initialState = {
    category: null,
    showAssignUserModal: false,
  };

  constructor(props) {
    super(props);
    this.state = CategoryEditModal.initialState;
    this.assignUser = this.assignUser.bind(this);
    this.removeAssignedUser = this.removeAssignedUser.bind(this);
    this.modalTitle = this.modalTitle.bind(this);
    this.updateCategory = this.updateCategory.bind(this);
    this.loadCategory = this.loadCategory.bind(this);
    this.deleteCategory = this.deleteCategory.bind(this);
  }

  componentDidUpdate(prevProps) {
    const { show } = this.props;

    if (!prevProps.show && show) {
      this.reset();
    }
  }

  reset() {
    this.setState(CategoryEditModal.initialState);
  }

  assignUser(assignedUsers, user) {
    this.setState({
      showAssignUserModal: false,
    });
    return assignedUsers.concat(user);
  }

  removeAssignedUser(assignedUsers, userId) {
    return assignedUsers.filter((user) => user.id !== userId);
  }

  modalTitle(category) {
    const { t } = this.props.i18n;
    return `${t("categories:category")}\u00A0-\u00A0${category.name}`;
  }

  async deleteCategory() {
    const { categoryId } = this.props;
    return CategoriesAPI.delete(categoryId);
  }

  async loadCategory() {
    const { categoryId } = this.props;
    return CategoriesAPI.getOne(categoryId);
  }

  async updateCategory(category) {
    const { categoryId } = this.props;

    const categoryData = {
      name: category.name,
      assignedUserIds: category.assignedUsers.map((user) => user.id),
    };
    return CategoriesAPI.update(categoryId, categoryData);
  }

  render() {
    const { show, onClose, onUpdate, onRemove } = this.props;
    const { t } = this.props.i18n;
    const { showAssignUserModal } = this.state;

    return (
      <EditModal
        title={this.modalTitle}
        update={this.updateCategory}
        onUpdate={onUpdate}
        remove={this.deleteCategory}
        onRemove={onRemove}
        onClose={onClose}
        show={show}
        load={this.loadCategory}
        validationSchema={Forms.Category.schema}
      >
        {({ values, errors, touched, handleChange, handleBlur, setFieldValue }) =>
          values && (
            <>
              <Form.Group>
                <Form.Label column="md">{t("categories:name.name")}</Form.Label>
                <Form.Control
                  required
                  name="name"
                  onBlur={handleBlur}
                  placeholder={t("categories:name.placeholder")}
                  value={values.name}
                  onChange={handleChange}
                  isInvalid={touched.name && errors.name}
                />
                <Form.Control.Feedback type="invalid">{errors.name}</Form.Control.Feedback>
              </Form.Group>
              <Form.Group className="mt-2">
                <Form.Label column="md">{t("categories:assignedUsers")}</Form.Label>
                <Button
                  size="sm"
                  variant="primary"
                  className="mt-2 float-end"
                  onClick={() => this.setState({ showAssignUserModal: true })}
                >
                  {t("categories:assignUser")}
                </Button>
                <DataTable.Table
                  size="sm"
                  noDataPlaceholderText={t("categories:noAssignedUsers")}
                  data={values.assignedUsers}
                  columns={Tables.CategoryAssignedUsers.columns}
                  events={{
                    onDelete: (userId) =>
                      setFieldValue(
                        "assignedUsers",
                        this.removeAssignedUser(values.assignedUsers, userId)
                      ),
                  }}
                />
              </Form.Group>

              <AuditedEntityMetadata entityData={values} />

              <AssignUserModal
                assignedUsers={values.assignedUsers}
                show={showAssignUserModal}
                onAssign={(user) =>
                  setFieldValue("assignedUsers", this.assignUser(values.assignedUsers, user))
                }
                onClose={() => this.setState({ showAssignUserModal: false })}
              />
            </>
          )
        }
      </EditModal>
    );
  }
}

CategoryEditModal.propTypes = {
  categoryId: PropTypes.number,
  show: PropTypes.bool,
  onUpdate: PropTypes.func,
  onRemove: PropTypes.func,
  onClose: PropTypes.func.isRequired,
};

CategoryEditModal.defaultProps = {
  categoryId: null,
  show: true,
  onUpdate: null,
  onRemove: null,
};

export default withTranslation(CategoryEditModal);
