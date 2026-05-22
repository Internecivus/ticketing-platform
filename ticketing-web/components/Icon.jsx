import React from "react";
import Tooltip from "react-bootstrap/Tooltip";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import Button from "react-bootstrap/Button";
import PropTypes from "prop-types";

class Icon extends React.Component {
  iconContainer(children) {
    const { containerClassName } = this.props;

    return <span className={`icon-container ${containerClassName}`}>{children}</span>;
  }

  tooltip(icon) {
    const { tooltip, variant } = this.props;
    return (
      <OverlayTrigger
        key="bottom"
        placement="bottom"
        overlay={<Tooltip id={variant}>{tooltip}</Tooltip>}
      >
        {icon}
      </OverlayTrigger>
    );
  }

  hrefIcon() {
    const { href } = this.props;
    return <a href={href}>{this.icon()}</a>;
  }

  buttonIcon() {
    const { onClick, buttonVariant } = this.props;
    return (
      <Button variant={buttonVariant} onClick={onClick}>
        {this.icon()}
      </Button>
    );
  }

  icon() {
    const { variant, size } = this.props;
    const iconSize = `icon-${size}`;
    return <i className={`icon ${iconSize} material-icons`}>{variant}</i>;
  }

  render() {
    const { onClick, href, tooltip } = this.props;
    let icon;
    let tooltipContainer;

    if (href) {
      icon = this.hrefIcon();
    } else if (onClick) {
      icon = this.buttonIcon();
    } else {
      icon = this.icon();
    }

    if (tooltip) {
      tooltipContainer = this.tooltip(icon);
      return this.iconContainer(tooltipContainer);
    }
    return this.iconContainer(icon);
  }
}

Icon.variant = {
  ACCOUNT: "account_circle",
  DELETE: "delete",
};

Icon.propTypes = {
  variant: PropTypes.oneOf(Object.values(Icon.variant)).isRequired,
  size: PropTypes.oneOf(["xs", "sm", "md", "lg", "xl"]),
  onClick: PropTypes.func,
  buttonVariant: PropTypes.oneOf(["primary", "secondary", "warning", "info", "danger"]),
  containerClassName: PropTypes.string,
  href: PropTypes.string,
  tooltip: PropTypes.string,
};

Icon.defaultProps = {
  size: "md",
  onClick: null,
  buttonVariant: "primary",
  containerClassName: null,
  href: null,
  tooltip: null,
};

export default Icon;
