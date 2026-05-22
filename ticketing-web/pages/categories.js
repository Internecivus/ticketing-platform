import withTranslation from "next-translate/withTranslation";
import React from "react";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import CategoryEditModal from "../components/category/CategoryEditModal";
import DefaultLayout from "../layouts/DefaultLayout";
import withAuthentication from "../components/withAuthentication";
import { CategoriesAPI } from "../app/api";
import Tables from "../app/tables";
import CreateModal from "../components/modal/CreateModal";
import { role } from "../app/authentication";
import DataTable from "../components/table/DataTable";
import Alert from "../components/Alert";
import Forms from "../app/forms";
import Head from "next/head";

class Categories extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showCategoryEditModal: false,
      showCategoryCreateModal: false,
      categories: props.categories,
      isLoadingCategories: false,
      showAlert: false,
      alert: {
        message: null,
        variant: null,
      },
    };
    this.categoryCreated = this.categoryCreated.bind(this);
    this.categoryDeleted = this.categoryDeleted.bind(this);
    this.categoryUpdated = this.categoryUpdated.bind(this);
    CategoriesAPI.create = CategoriesAPI.create.bind(CategoriesAPI);
  }

  static async getInitialProps() {
    const categories = await CategoriesAPI.getAll();
    return {
      categories,
    };
  }

  selectCategory(id) {
    this.setState({
      selectedCategoryId: id,
      showCategoryEditModal: true,
    });
  }

  async loadCategories() {
    this.setState({ isLoadingCategories: true });
    const categories = await CategoriesAPI.getAll();
    this.setState({
      categories,
      isLoadingCategories: false,
    });
  }

  categoryCreated(category) {
    const { t } = this.props.i18n;
    this.setState({
      alert: {
        message: t("categories:message.created", { categoryName: category.name }),
        variant: "success",
      },
      showCategoryCreateModal: false,
      showAlert: true,
    });
    this.loadCategories();
  }

  categoryUpdated(category) {
    const { t } = this.props.i18n;
    this.setState({
      alert: {
        message: t("categories:message.updated", { categoryName: category.name }),
        variant: "success",
      },
      showCategoryEditModal: false,
      showAlert: true,
    });
    this.loadCategories();
  }

  categoryDeleted(category) {
    const { t } = this.props.i18n;
    this.setState({
      alert: {
        message: t("categories:message.deleted", { categoryName: category.name }),
        variant: "success",
      },
      showCategoryEditModal: false,
      showAlert: true,
    });
    this.loadCategories();
  }

  render() {
    const { t } = this.props.i18n;
    const {
      categories,
      showAlert,
      showCategoryEditModal,
      showCategoryCreateModal,
      selectedCategoryId,
      alert,
      isLoadingCategories,
    } = this.state;
    return (
      <DefaultLayout>
        <Head>
          <title>{t("categories:title")}</title>
        </Head>
        <Row>
          <Col className="mx-lg-5 mt-1">
            <h1 className="mb-5">{t("categories:title")}</h1>
            <Alert
              data={alert}
              show={showAlert}
              onClose={() => this.setState({ showAlert: false })}
            />
            <Button
              variant="success"
              className="mb-3"
              onClick={() => this.setState({ showCategoryCreateModal: true })}
            >
              {t("categories:createCategory")}
            </Button>
            <DataTable.Table
              data={categories}
              columns={Tables.Categories.columns}
              noDataPlaceholderText={Tables.Categories.noDataPlaceholder}
              events={{ onClick: (id) => this.selectCategory(id) }}
              isLoading={isLoadingCategories}
            />
            <CreateModal
              title={t("categories:newCategory")}
              show={showCategoryCreateModal}
              validationSchema={Forms.Category.schema}
              initialValues={Forms.Category.defaultValues}
              create={CategoriesAPI.create}
              onCreate={this.categoryCreated}
              onClose={() => this.setState({ showCategoryCreateModal: false })}
            >
              {({ values, errors, touched, handleChange, handleBlur }) => (
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
              )}
            </CreateModal>
            <CategoryEditModal
              categoryId={selectedCategoryId}
              show={showCategoryEditModal}
              onUpdate={this.categoryUpdated}
              onRemove={this.categoryDeleted}
              onClose={() => this.setState({ showCategoryEditModal: false })}
            />
          </Col>
        </Row>
      </DefaultLayout>
    );
  }
}

export default withTranslation(withAuthentication(role.CATEGORY_ADMINISTRATION, Categories));
