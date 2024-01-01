import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVehicleLicenseType } from 'app/shared/model/vehicle-license-type.model';
import { getEntity, updateEntity, createEntity, reset } from './vehicle-license-type.reducer';

export const VehicleLicenseTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehicleLicenseTypeEntity = useAppSelector(state => state.gatewayapp.vehicleLicenseType.entity);
  const loading = useAppSelector(state => state.gatewayapp.vehicleLicenseType.loading);
  const updating = useAppSelector(state => state.gatewayapp.vehicleLicenseType.updating);
  const updateSuccess = useAppSelector(state => state.gatewayapp.vehicleLicenseType.updateSuccess);

  const handleClose = () => {
    navigate('/vehicle-license-type' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.rank !== undefined && typeof values.rank !== 'number') {
      values.rank = Number(values.rank);
    }

    const entity = {
      ...vehicleLicenseTypeEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...vehicleLicenseTypeEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.vehicleLicenseType.home.createOrEditLabel" data-cy="VehicleLicenseTypeCreateUpdateHeading">
            <Translate contentKey="gatewayApp.vehicleLicenseType.home.createOrEditLabel">Create or edit a VehicleLicenseType</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="vehicle-license-type-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.vehicleLicenseType.name')}
                id="vehicle-license-type-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('gatewayApp.vehicleLicenseType.rank')}
                id="vehicle-license-type-rank"
                name="rank"
                data-cy="rank"
                type="text"
              />
              <ValidatedField
                label={translate('gatewayApp.vehicleLicenseType.engName')}
                id="vehicle-license-type-engName"
                name="engName"
                data-cy="engName"
                type="text"
              />
              <ValidatedField
                label={translate('gatewayApp.vehicleLicenseType.code')}
                id="vehicle-license-type-code"
                name="code"
                data-cy="code"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vehicle-license-type" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default VehicleLicenseTypeUpdate;
