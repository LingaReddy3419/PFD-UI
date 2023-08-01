import './home.scss';
import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate, Navigate } from 'react-router-dom';
import AddIcon from '@mui/icons-material/Add';
import CancelIcon from '@mui/icons-material/Cancel';
import {
  Button,
  Typography,
  Grid,
  IconButton,
  Card,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  TextField,
  Container,
  Modal,
} from '@mui/material';
import { useAppSelector } from 'app/config/store';
import { Formik, Form, Field, ErrorMessage, useFormik, FormikHelpers } from 'formik';
import * as Yup from 'yup';
import { DragDropContext, Droppable, Draggable } from 'react-beautiful-dnd';
import axios from 'axios';
import { identity } from 'lodash';

const availableMoleculeTypes = ['General', 'High Potent', 'Amidites'];

const availableUnits = ['II', 'III', 'IV'];
const availableOperations = ['Operation 1', 'Operation 2', 'Operation 3'];
const availableScbs = ['SCB1', 'SCB2', 'SCB3'];
const availablePhValues = ['Basic', 'Acidic'];
const validationSchema = Yup.object().shape({
  title: Yup.string().required('Title is required'),
  description: Yup.string().required('Description is required'),
});

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const [table, setTable] = useState([]);
  const [x, setX] = useState(0);
  const [showOperations, setShowOperations] = useState(false);
  const [isSaved, setIsSaved] = useState(false);
  const [isOpen, setIsOpen] = useState(false);
  // const [availableMoleculeTypes, setAvailableMoleculeTypes] = useState([]);
  // const [dropdownValueMoleculeType, setDropdownValueMoleculeType] = useState('general');
  // const [dropdownValueUnit, setDropdownValueUnit] = useState('iv');
  // const [dropdownValueOperation,setDropdownValueOperation]=useState('operation1');
  // const [dropdownValueScb,setDropdownValueScb]=useState('scb1');
  // const [dropdownValuePhValue,setDropdownValuePhValue]=useState('phValue1');
  // const [outputQty, setOutputQty] = useState('');
  // const [inputQty, setInputQty] = useState('');
  // const [expectedYield, setExpectedYield] = useState('');
  // const [maxVolQty, setMaxVolQty] = useState('');
  // const [minVolQty, setMinVolQty] = useState('');

  const formik = useFormik({
    initialValues: {
      [`name${x}`]: '',
      [`age${x}`]: '',
      [`qualification${x}`]: '',
      [`decisionMaking${x}`]: '',
    },
    onSubmit: values => {
      console.log(values);
    },
  });
  const formikProps = useFormik({
    initialValues: {
      moleculeType: 'General',
      unit: 'IV',
      operation: 'Operation 1',
      scb: 'SCB1',
      outputQty: '',
      inputQty: '',
      expectedYield: '',
      phValue: 'Basic',
      maxVolQty: '',
      minVolQty: '',
    },
    onSubmit: values => {
      // Handle form submission logic if needed
      console.log(values);
    },
  });

  const formikTitle = useFormik({
    initialValues: {
      title: '',
      description: '',
    },
    validationSchema: validationSchema,
    onSubmit: values => {
      setIsSaved(true);
      console.log(values);
    },
  });

  const openModal = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    setIsOpen(false);
  };

  const handleAddMore = () => {
    if (x === 0) {
      setIsOpen(true);
    }
    const newTable = {
      index: x,
      rows: [
        {
          [`name${x}`]: '',
          [`age${x}`]: '',
          [`qualification${x}`]: '',
          [`decisionMaking${x}`]: '',
        },
      ],
    };

    setTable([...table, newTable]);
    setX(x + 1);
  };

  const removeHandler = id => {
    setTable(
      table?.filter((item, index) => {
        return id !== index;
      })
    );
  };

  const handleInsideAddHandler = id => {
    setTable(prevTable => {
      const updatedTable = [...prevTable];
      const row = updatedTable.find(item => item.index === id);

      row?.rows.push({
        [`name${row?.rows.length}`]: '',
        [`age${row?.rows.length}`]: '',
        [`qualification${row?.rows.length}`]: '',
        [`decisionMaking${row?.rows.length}`]: '',
      });
      return updatedTable;
    });
  };

  const handleRemoveHandler = (id, id1) => {
    setTable(
      table.map(item => {
        if (item.index === id) {
          return {
            ...item,
            rows: item.rows.filter((row, rowIndex) => {
              return rowIndex !== id1;
            }),
          };
        }
        return item;
      })
    );
  };

  const onDragEnd = result => {
    const { destination, source } = result;
    if (!destination) {
      return;
    }
    if (destination.droppableId === source.droppableId && destination.index === source.index) {
      return;
    }

    const updatedTable = [...table];
    const draggedItem = updatedTable[source.index];

    // Remove item from source position
    updatedTable.splice(source.index, 1);

    // Insert item into destination position
    updatedTable.splice(destination.index, 0, draggedItem);

    setTable(updatedTable);
  };

  // const handleInputChange = (event) => {
  //   const { name, value } = event.target;
  //   if (name === 'molecule') {
  //     setDropdownValueMoleculeType(value);
  //   }else if (name === 'unit'){
  //     setDropdownValueUnit(value);
  //   }else if (name==="operation"){
  //     setDropdownValueOperation(value)
  //   }else if (name==="scb"){
  //     setDropdownValueScb(value)
  //   }
  //    else if (name === 'output') {
  //     setOutputQty(value);
  //   }else if (name==="yield"){
  //     setExpectedYield(value);
  //   }else if (name === 'inputQty') {
  //     setInputQty(value);
  //   }else if(name==="phValue"){
  //     setDropdownValuePhValue(value)
  //   }
  //   else if(name==="maxVolume"){
  //     setMaxVolQty(value);
  //   }else if(name==="minVolume"){
  //     setMinVolQty(value);
  //   }
  // };

  const handleModalSubmit = () => {
    // Handle the modal-specific submit logic here
    console.log('Modal Submitted:', formikProps.values);
    setShowOperations(true);
    formikProps.resetForm();
    setIsOpen(false);
    handleClose();
  };

  // const getMoleculeTypes = async () => {
  //   try {
  //     const response = await axios.get('http://192.168.1.6:8080/api/molecule-types/id-title');
  //     setAvailableMoleculeTypes(response?.data);
  //   } catch (error) {
  //     console.error('Error fetching data:', error);
  //   }
  // };

  // useEffect(() => {
  //   getMoleculeTypes();
  // }, []);

  // console.log(availableMoleculeTypes)
  return (
    <>
      <Modal open={isOpen} onClose={handleClose}>
        <div className="modal-content">
          <span className="close" onClick={handleClose}>
            <CancelIcon />
          </span>
          <h2 style={{ marginBottom: '20px' }}>Available Reactors</h2>
          <Grid container spacing={3}>
            <Grid item xs={3}>
              <FormControl fullWidth variant="outlined" size="small">
                <InputLabel htmlFor="molecule">Molecule Type</InputLabel>
                <Select
                  id="molecule"
                  name="moleculeType"
                  value={formikProps.values.moleculeType}
                  onChange={formikProps.handleChange}
                  label="Molecule Type"
                >
                  {availableMoleculeTypes.map(eachMolecule => {
                    // const { id, title } = eachMolecule;
                    return (
                      <MenuItem key={eachMolecule} value={eachMolecule}>
                        {eachMolecule}
                      </MenuItem>
                    );
                  })}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={3}>
              <FormControl fullWidth variant="outlined" size="small">
                <InputLabel htmlFor="unit">UNIT</InputLabel>
                <Select id="unit" name="unit" value={formikProps.values.unit} onChange={formikProps.handleChange} label="UNIT">
                  {availableUnits.map(unit => (
                    <MenuItem key={unit} value={unit}>
                      {unit}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={3}>
              <FormControl fullWidth variant="outlined" size="small">
                <InputLabel htmlFor="operation">Operation</InputLabel>
                <Select
                  id="operation"
                  name="operation"
                  value={formikProps.values.operation}
                  onChange={formikProps.handleChange}
                  label="Operation"
                >
                  {availableOperations.map(operation => (
                    <MenuItem key={operation} value={operation}>
                      {operation}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={3}>
              <FormControl fullWidth variant="outlined" size="small">
                <InputLabel htmlFor="scb">SCB</InputLabel>
                <Select id="scb" name="scb" value={formikProps.values.scb} onChange={formikProps.handleChange} label="SCB">
                  {availableScbs.map(scb => (
                    <MenuItem key={scb} value={scb}>
                      {scb}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={4}>
              <TextField
                fullWidth
                size="small"
                id="output"
                name="outputQty"
                label="Output Batch Quantity, Kg"
                variant="outlined"
                value={formikProps.values.outputQty}
                onChange={formikProps.handleChange}
              />
            </Grid>
            <Grid item xs={4}>
              <TextField
                fullWidth
                size="small"
                id="yield"
                name="expectedYield"
                label="Expected Yield"
                variant="outlined"
                value={formikProps.values.expectedYield}
                onChange={formikProps.handleChange}
              />
            </Grid>
            <Grid item xs={4}>
              <TextField
                fullWidth
                size="small"
                id="inputQty"
                name="inputQty"
                label="Input Batch Quantity, Kg"
                variant="outlined"
                value={formikProps.values.inputQty}
                onChange={formikProps.handleChange}
              />
            </Grid>
            <Grid item xs={4}>
              <FormControl fullWidth variant="outlined" size="small">
                <InputLabel htmlFor="phValue">Acidic/Basic</InputLabel>
                <Select
                  id="phValue"
                  name="phValue"
                  value={formikProps.values.phValue}
                  onChange={formikProps.handleChange}
                  label="Acidic/Basic"
                >
                  {availablePhValues.map(value => (
                    <MenuItem key={value} value={value}>
                      {value}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={4}>
              <TextField
                fullWidth
                size="small"
                id="maxVolQty"
                name="maxVolQty"
                label="Maximum Reaction Volume, L"
                variant="outlined"
                value={formikProps.values.maxVolQty}
                onChange={formikProps.handleChange}
              />
            </Grid>
            <Grid item xs={4}>
              <TextField
                fullWidth
                size="small"
                id="minVolQty"
                name="minVolQty"
                label="Minimum Reaction Volume, L"
                variant="outlined"
                value={formikProps.values.minVolQty}
                onChange={formikProps.handleChange}
              />
            </Grid>
            <Grid item xs={2}>
              <Button variant="contained" fullWidth onClick={handleClose} color="error">
                Close
              </Button>
            </Grid>
            <Grid item xs={2}>
              <Button variant="contained" fullWidth onClick={handleModalSubmit} color="success">
                Save
              </Button>
            </Grid>
          </Grid>
        </div>
      </Modal>
      {account.login === 'admin' && (
        <div>
          <form onSubmit={formikTitle.handleSubmit}>
            <div style={{ display: 'flex' }}>
              <TextField
                size="small"
                id="title"
                name="title"
                label="Title"
                variant="outlined"
                value={formikTitle.values.title}
                onChange={formikTitle.handleChange}
                onBlur={formikTitle.handleBlur}
                error={formikTitle.touched.title && Boolean(formikTitle.errors.title)}
                helperText={formikTitle.touched.title && formikTitle.errors.title}
                sx={{ width: '40%' }}
              />
              <TextField
                size="small"
                id="description"
                name="description"
                label="Description"
                variant="outlined"
                value={formikTitle.values.description}
                onChange={formikTitle.handleChange}
                onBlur={formikTitle.handleBlur}
                error={formikTitle.touched.description && Boolean(formikTitle.errors.description)}
                helperText={formikTitle.touched.description && formikTitle.errors.description}
                sx={{ width: '60%', marginLeft: '20px' }}
              />
            </div>
            <div style={{ display: 'flex', marginTop: isSaved ? '10px' : '5px' }}>
              <Button type="submit" variant="contained" sx={{ marginLeft: 'auto' }}>
                Save
              </Button>
            </div>
          </form>
          {isSaved && (
            <div
              style={{
                display: 'flex',
                flexDirection: 'row',
                justifyContent: 'space-between',
                margin: '10px',
              }}
            >
              <Typography variant="h5" style={{ display: 'flex', alignItems: 'center' }}>
                Process Flow:{' '}
                <span style={{ marginLeft: '10px', fontSize: '15px', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  {formikTitle.values.title}
                </span>
              </Typography>
              <Typography variant="h5" style={{ display: 'flex', alignItems: 'center' }}>
                Description:{' '}
                <span style={{ marginLeft: '10px', fontSize: '15px', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  {formikTitle.values.description}
                </span>
              </Typography>
              <Button size="small" variant="contained" color="primary" startIcon={<AddIcon fontSize="small" />} onClick={handleAddMore}>
                Add New Operation
              </Button>
            </div>
          )}
          {showOperations && (
            <DragDropContext onDragEnd={onDragEnd}>
              <div style={{ padding: '20px' }}>
                {table?.map((item1, index) => (
                  <>
                    <Typography>Operation {index + 1}</Typography>
                    <Droppable droppableId={`droppable-${item1.index}`} key={item1.index}>
                      {(provided, snapshot) => (
                        <div
                          ref={provided.innerRef}
                          {...provided.droppableProps}
                          style={{
                            border: '1px solid grey',
                            marginBottom: '10px',
                            backgroundColor: index % 2 === 0 ? '#f6f6f6' : '#FFFFFF',
                          }}
                        >
                          <Draggable draggableId={`draggable-${item1.index}`} index={index} key={`draggable-${item1.index}`}>
                            {(provided, snapshot) => (
                              <div ref={provided.innerRef} {...provided.draggableProps} {...provided.dragHandleProps}>
                                <form onSubmit={formik.handleSubmit}>
                                  <Container sx={{ display: 'flex', justifyContent: 'space-between' }}>
                                    <Typography variant="h6" sx={{ paddingLeft: '10px', paddingTop: '10px' }}>
                                      Operation Head {index + 1}
                                    </Typography>
                                    <IconButton onClick={() => removeHandler(index)}>
                                      <CancelIcon fontSize="small" />
                                    </IconButton>
                                  </Container>
                                  <Card
                                    style={{
                                      display: 'flex',
                                      flexDirection: 'row',
                                      justifyContent: 'space-between',
                                      backgroundColor: index % 2 === 0 ? '#f6f6f6' : '#ffffff',
                                      marginLeft: '6px',
                                      marginRight: '6px',
                                      marginBottom: '6px',
                                      padding: '6px',
                                    }}
                                  >
                                    <Grid
                                      container
                                      sx={{
                                        display: 'flex',
                                        flexDirection: 'row',
                                        alignItems: 'center',
                                      }}
                                    >
                                      <Grid
                                        item
                                        xs={1.5}
                                        sx={{
                                          display: 'flex',
                                          flexDirection: 'column',
                                          alignItems: 'center',
                                          justifyContent: 'center',
                                          marginRight: '10px',
                                          marginTop: '20px',
                                        }}
                                      >
                                        <FormControl sx={{ m: 1 }} fullWidth size="small">
                                          <InputLabel id="demo-simple-select-label-operation">Operation</InputLabel>
                                          <Select
                                            labelId="demo-simple-select-label-operation"
                                            id="demo-simple-select"
                                            value={'reaction'}
                                            label="Operation"
                                            onChange={formik.handleChange}
                                            size="small"
                                          >
                                            <MenuItem value={'reaction'} selected defaultChecked>
                                              Reaction
                                            </MenuItem>
                                            <MenuItem value={'Quenching'}>Quenching</MenuItem>
                                            <MenuItem value={'Work-up'}>Work-up</MenuItem>
                                            <MenuItem value={'Distillation'}>Distillation</MenuItem>
                                            <MenuItem value={'pH Adjustment'}>pH Adjustment</MenuItem>
                                            <MenuItem value={'Isolation'}>Isolation</MenuItem>
                                            <MenuItem value={'Filtration'}>Filtration</MenuItem>
                                            <MenuItem value={'Drying'}>Drying</MenuItem>
                                          </Select>
                                        </FormControl>
                                      </Grid>
                                      <Grid
                                        item
                                        xs={1.5}
                                        sx={{
                                          display: 'flex',
                                          flexDirection: 'column',
                                          alignItems: 'center',
                                          justifyContent: 'center',
                                          marginRight: '10px',
                                          marginTop: '20px',
                                        }}
                                      >
                                        <FormControl sx={{ m: 1 }} fullWidth size="small">
                                          <InputLabel id="demo-simple-select-label-reactor">Reactor ID</InputLabel>
                                          <Select
                                            labelId="demo-simple-select-label-reactor"
                                            id="demo-simple-select"
                                            value={'reaction'}
                                            label="Reactor ID"
                                            onChange={formik.handleChange}
                                            size="small"
                                          >
                                            <MenuItem value={'reaction'}>BSAC14-1</MenuItem>
                                            <MenuItem value={'Quenching'}>BSAC15-1</MenuItem>
                                            <MenuItem value={'Work-up'}>BSAC16-1</MenuItem>
                                            <MenuItem value={'Distillation'}>BSAC17-1</MenuItem>
                                            <MenuItem value={'pH Adjustment'}>BSAC18-1</MenuItem>
                                            <MenuItem value={'Isolation'}>BSAC19-1</MenuItem>
                                            <MenuItem value={'Filtration'}>BSAC20-1</MenuItem>
                                            <MenuItem value={'Drying'}>BAGR03</MenuItem>
                                          </Select>
                                        </FormControl>
                                      </Grid>
                                      <Grid
                                        item
                                        xs={1.5}
                                        sx={{
                                          display: 'flex',
                                          flexDirection: 'column',
                                          alignItems: 'center',
                                          justifyContent: 'center',
                                          marginRight: '10px',
                                          marginTop: '20px',
                                        }}
                                      >
                                        <FormControl sx={{ m: 1 }} fullWidth size="small">
                                          <InputLabel id="demo-simple-select-label-operation">Block</InputLabel>
                                          <Select
                                            labelId="demo-simple-select-label-operation"
                                            id="demo-simple-select"
                                            value={'block'}
                                            label="Block"
                                            onChange={formik.handleChange}
                                            size="small"
                                          >
                                            <MenuItem value={'block'} selected defaultChecked>
                                              Block 1
                                            </MenuItem>
                                            <MenuItem value={'Quenching'}>Block 2</MenuItem>
                                            <MenuItem value={'Work-up'}>Block 3</MenuItem>
                                          </Select>
                                        </FormControl>
                                      </Grid>
                                      <Grid
                                        item
                                        xs={1.5}
                                        sx={{
                                          display: 'flex',
                                          flexDirection: 'column',
                                          alignItems: 'center',
                                          justifyContent: 'center',
                                          marginRight: '10px',
                                        }}
                                      >
                                        Working Volume
                                        <TextField size="small" disabled value={'Working Vol'} />
                                      </Grid>
                                      <Grid
                                        item
                                        xs={1.6}
                                        sx={{
                                          display: 'flex',
                                          flexDirection: 'column',
                                          alignItems: 'center',
                                          justifyContent: 'center',
                                          marginRight: '10px',
                                        }}
                                      >
                                        Min Stirable Vol
                                        <TextField size="small" disabled value={'Min Stirable Vol'} />
                                      </Grid>
                                      <Grid
                                        item
                                        xs={1.7}
                                        sx={{
                                          display: 'flex',
                                          flexDirection: 'column',
                                          alignItems: 'center',
                                          justifyContent: 'center',
                                          marginRight: '10px',
                                        }}
                                      >
                                        Sensing Volume
                                        <TextField size="small" disabled value={'Sensing Volume'} />
                                      </Grid>
                                      <Grid
                                        item
                                        xs={1.8}
                                        sx={{
                                          display: 'flex',
                                          flexDirection: 'column',
                                          alignItems: 'center',
                                          justifyContent: 'center',
                                          marginRight: '10px',
                                        }}
                                      >
                                        Agitator
                                        <TextField size="small" disabled value={'Agitator'} />
                                      </Grid>
                                    </Grid>
                                  </Card>
                                  {item1?.rows?.map((item, insideIndex) => (
                                    <Grid
                                      container
                                      spacing={2}
                                      sx={{
                                        padding: '10px',
                                        display: 'flex',
                                        alignItems: 'center',
                                        // border:'1px solid grey'
                                      }}
                                      key={`${insideIndex}-${item1.index}`}
                                    >
                                      <Grid item xs={12}>
                                        <Typography>Action {insideIndex + 1}</Typography>
                                      </Grid>
                                      <Grid item xs={1.5}>
                                        <FormControl sx={{ m: 1 }} fullWidth size="small">
                                          <InputLabel id="demo-simple-select-helper-label">Action</InputLabel>
                                          <Select
                                            labelId="demo-simple-select-helper-label"
                                            id="demo-simple-select-helper"
                                            value={10}
                                            label="Action"
                                            onChange={formik.handleChange}
                                          >
                                            <MenuItem value={10}>Charge</MenuItem>
                                            <MenuItem value={20}>Add</MenuItem>
                                            <MenuItem value={30}>Maintainence</MenuItem>
                                            <MenuItem value={30}>Heat</MenuItem>
                                            <MenuItem value={30}>Cool</MenuItem>
                                            <MenuItem value={30}>IPC</MenuItem>
                                            <MenuItem value={30}>Unload</MenuItem>
                                            <MenuItem value={30}>Apply Vacuum</MenuItem>
                                          </Select>
                                        </FormControl>
                                        {/* <TextField
                                size="small"
                                fullWidth
                                label="Name"
                                name={`name${insideIndex}${item1.index}`}
                                onChange={formik.handleChange}
                                value={
                                  formik.values[
                                    `name${insideIndex}${item1.index}`
                                  ]
                                }
                              /> */}
                                      </Grid>
                                      <Grid item xs={1.5}>
                                        <FormControl sx={{ m: 1 }} fullWidth size="small">
                                          <InputLabel id="demo-simple-select-helper-label">Raw Material</InputLabel>
                                          <Select
                                            labelId="demo-simple-select-helper-label"
                                            id="demo-simple-select-helper"
                                            value={10}
                                            label="Raw Material"
                                            onChange={formik.handleChange}
                                          >
                                            <MenuItem value={10}>L-(+)-Lactic Acid</MenuItem>
                                            <MenuItem value={20}>Ethanol (Lot-I)</MenuItem>
                                            <MenuItem value={30}>Milli Q Water</MenuItem>
                                            <MenuItem value={20}>Ethanol (Lot-II)</MenuItem>
                                            <MenuItem value={20}>Ethanol (Lot-III)</MenuItem>
                                            <MenuItem value={20}>Ethanol (Lot-IV)</MenuItem>
                                          </Select>
                                        </FormControl>
                                        {/* <TextField
                                size="small"
                                fullWidth
                                label="Age"
                                name={`age${insideIndex}${item1.index}`}
                                onChange={formik.handleChange}
                                value={
                                  formik.values[
                                    `age${insideIndex}${item1.index}`
                                  ]
                                }
                              /> */}
                                      </Grid>
                                      <Grid
                                        item
                                        xs={1.5}
                                        sx={{
                                          display: 'flex',
                                          alignItems: 'center',
                                          justifyContent: 'center',
                                        }}
                                      >
                                        <FormControl sx={{ m: 1 }} fullWidth size="small">
                                          <InputLabel id="demo-simple-select-helper-label">UOM</InputLabel>
                                          <Select
                                            labelId="demo-simple-select-helper-label"
                                            id="demo-simple-select-helper"
                                            value={10}
                                            label="UOM"
                                            onChange={formik.handleChange}
                                          >
                                            <MenuItem value={10}>Kg</MenuItem>
                                            <MenuItem value={20}>L</MenuItem>
                                          </Select>
                                        </FormControl>
                                        {/* <TextField
                                size="small"
                                fullWidth
                                label="Qualification"
                                name={`qualification${insideIndex}${item1.index}`}
                                onChange={formik.handleChange}
                                value={
                                  formik.values[
                                    `qualification${insideIndex}${item1.index}`
                                  ]
                                }
                                sx={{ marginBottom: "1rem" }}
                              /> */}
                                      </Grid>
                                      <Grid
                                        item
                                        xs={1.5}
                                        sx={{
                                          display: 'flex',
                                          flexDirection: 'column',
                                          alignItems: 'center',
                                          justifyContent: 'flex-start',
                                          marginTop: '-20px',
                                        }}
                                      >
                                        <Typography>Quantity</Typography>
                                        <TextField value={10.004} disabled size="small" />
                                        {/* <TextField
                                label="Decision Making"
                                name={`decisionMaking${insideIndex}${item1.index}`}
                                value={
                                  formik.values[
                                    `decisionMaking${insideIndex}${item1.index}`
                                  ]
                                }
                                onChange={formik.handleChange}
                                fullWidth
                                size="small"
                              /> */}
                                      </Grid>
                                      <Grid
                                        item
                                        xs={1.5}
                                        sx={{
                                          display: 'flex',
                                          alignItems: 'center',
                                          justifyContent: 'center',
                                        }}
                                      >
                                        <FormControl sx={{ m: 1 }} fullWidth size="small">
                                          <InputLabel id="demo-simple-select-helper-label">Mode Of Charging</InputLabel>
                                          <Select
                                            labelId="demo-simple-select-helper-label"
                                            id="demo-simple-select-helper"
                                            value={10}
                                            label="Mode Of Charging"
                                            onChange={formik.handleChange}
                                          >
                                            <MenuItem value={10}>Mode Of Charging 1</MenuItem>
                                            <MenuItem value={20}>Mode Of Charging 2</MenuItem>
                                            <MenuItem value={10}>Mode Of Charging 3</MenuItem>
                                            <MenuItem value={20}>Mode Of Charging 4</MenuItem>
                                          </Select>
                                        </FormControl>
                                        {/* <TextField
                                size="small"
                                fullWidth
                                label="Qualification"
                                name={`qualification${insideIndex}${item1.index}`}
                                onChange={formik.handleChange}
                                value={
                                  formik.values[
                                    `qualification${insideIndex}${item1.index}`
                                  ]
                                }
                                sx={{ marginBottom: "1rem" }}
                              /> */}
                                      </Grid>
                                      <Grid
                                        item
                                        xs={4.5}
                                        sx={{
                                          display: 'flex',
                                          flexDirection: 'column',
                                          alignItems: 'center',
                                          // border: "1px solid grey",
                                          marginTop: '-40px',
                                          padding: '4px',
                                        }}
                                      >
                                        <Typography variant="body1">Process Conditions</Typography>
                                        <Container
                                          sx={{
                                            display: 'flex',
                                          }}
                                        >
                                          <Container
                                            sx={{
                                              display: 'flex',
                                              flexDirection: 'column',
                                              justifyContent: 'flex-start',
                                              alignItems: 'center',
                                              width: '60pc',
                                            }}
                                          >
                                            <Typography variant="body2" style={{ fontSize: '12px' }}>
                                              Target Temp("C)
                                            </Typography>
                                            <TextField
                                              size="small"
                                              // style={{ height: "20px" }}
                                            />
                                          </Container>
                                          <Container
                                            sx={{
                                              display: 'flex',
                                              flexDirection: 'column',
                                              alignItems: 'center',
                                            }}
                                          >
                                            <Typography style={{ fontSize: '12px' }}>NA</Typography>
                                            <TextField
                                              size="small"
                                              // style={{ height: "20px" }}
                                            />
                                          </Container>
                                          <Container
                                            sx={{
                                              display: 'flex',
                                              flexDirection: 'column',
                                              alignItems: 'center',
                                            }}
                                          >
                                            <Typography style={{ fontSize: '12px' }}>NA</Typography>
                                            <TextField
                                              size="small"
                                              // style={{ height: "20px" }}
                                            />
                                          </Container>
                                        </Container>
                                      </Grid>
                                      <Grid
                                        item
                                        xs={3}
                                        sx={{
                                          display: 'flex',
                                          flexDirection: 'column',
                                          alignItems: 'center',
                                          // border: "1px solid grey",
                                          marginLeft: '10px',
                                          marginTop: '-20px',
                                        }}
                                      >
                                        <Typography variant="body1">Volumes</Typography>
                                        <Container
                                          sx={{
                                            display: 'flex',
                                          }}
                                        >
                                          <Container
                                            sx={{
                                              display: 'flex',
                                              flexDirection: 'column',
                                              alignItems: 'center',
                                            }}
                                          >
                                            <Typography variant="body2" style={{ fontSize: '12px' }}>
                                              Min(L)
                                            </Typography>
                                            <TextField size="small" style={{ height: '20px' }} />
                                          </Container>
                                          <Container
                                            sx={{
                                              display: 'flex',
                                              flexDirection: 'column',
                                              alignItems: 'center',
                                            }}
                                          >
                                            <Typography variant="body2" style={{ fontSize: '12px' }}>
                                              Max(L)
                                            </Typography>
                                            <TextField size="small" style={{ height: '20px' }} />
                                          </Container>
                                        </Container>
                                      </Grid>
                                      <Grid
                                        item
                                        xs={3}
                                        sx={{
                                          display: 'flex',
                                          flexDirection: 'column',
                                          alignItems: 'center',
                                          // border: "1px solid grey",
                                          marginLeft: '10px',
                                          marginTop: '-20px',
                                        }}
                                      >
                                        <Typography variant="body1">Occupancy</Typography>
                                        <Container
                                          sx={{
                                            display: 'flex',
                                          }}
                                        >
                                          <Container
                                            sx={{
                                              display: 'flex',
                                              flexDirection: 'column',
                                              alignItems: 'center',
                                            }}
                                          >
                                            <Typography variant="body2" style={{ fontSize: '12px' }}>
                                              Min %
                                            </Typography>
                                            <TextField size="small" style={{ height: '20px' }} />
                                          </Container>
                                          <Container
                                            sx={{
                                              display: 'flex',
                                              flexDirection: 'column',
                                              alignItems: 'center',
                                            }}
                                          >
                                            <Typography variant="body2" style={{ fontSize: '12px' }}>
                                              Max %
                                            </Typography>
                                            <TextField size="small" style={{ height: '20px' }} />
                                          </Container>
                                        </Container>
                                      </Grid>
                                      <Grid
                                        item
                                        xs={4}
                                        sx={{
                                          display: 'flex',
                                          flexDirection: 'column',
                                          alignItems: 'center',
                                          // border: "1px solid grey",
                                          margin: '10px',
                                          padding: '10px',
                                        }}
                                      >
                                        <Typography>Special Instructions</Typography>
                                        <TextField fullWidth size="small" style={{ height: '20px' }} />
                                      </Grid>
                                      <Grid item xs={1.5}>
                                        <IconButton
                                          onClick={() => {
                                            handleInsideAddHandler(item1.index);
                                          }}
                                        >
                                          <AddIcon fontSize="small" />
                                        </IconButton>
                                        <IconButton
                                          onClick={() => {
                                            handleRemoveHandler(item1.index, insideIndex);
                                          }}
                                        >
                                          <CancelIcon fontSize="small" />
                                        </IconButton>
                                      </Grid>
                                    </Grid>
                                  ))}
                                </form>
                              </div>
                            )}
                          </Draggable>
                          {provided.placeholder}
                        </div>
                      )}
                    </Droppable>
                  </>
                ))}
              </div>
            </DragDropContext>
          )}
        </div>
      )}

      {account.login !== 'admin' && (
        <div>
          <Grid container style={{ height: '100vh', display: 'flex' }}>
            <Grid item xs={6} style={{ display: 'flex', padding: '20px' }}>
              <Container style={{ width: '20%' }}>
                <img
                  src="https://www.lifescienceintegrates.com/wp-content/uploads/2020/09/Sai-logo.jpg"
                  alt="Logo"
                  style={{ width: '100%', height: '20%' }}
                />
              </Container>
              <Container style={{ display: 'flex', flexDirection: 'column', width: '80%', paddingTop: '30px' }}>
                <Typography variant="h5" style={{ color: '#f1592a', fontWeight: 'bold' }}>
                  A Trusted Partner in bringing medicines to life. Fast
                </Typography>
                <img
                  src="https://www.sailife.com/wp-content/uploads/2022/05/The-Sai-Way-810x540.jpg"
                  alt="Image"
                  style={{ width: '100%', height: '80%', borderRadius: '100%', marginTop: '20px' }}
                />
              </Container>
            </Grid>
            <Grid
              item
              xs={6}
              style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
              }}
              className="login-box-container"
            >
              <Container style={{ display: 'flex', flexDirection: 'column', width: '70%' }}>
                {/* <Typography variant="h6" style={{ color: '#ffffff' }}>
                  Login Page
                </Typography>
                <TextField
                  label="Username"
                  fullWidth
                  margin="normal"
                  InputLabelProps={inputLabelProps}
                  InputProps={inputProps}
                  value={'admin'}
                />
                <TextField
                  label="Password"
                  type="password"
                  fullWidth
                  margin="normal"
                  InputLabelProps={inputLabelProps}
                  InputProps={inputProps}
                  value={'admin'}
                /> */}
                {/* <Button
            variant="contained"
            color="secondary"
            style={{ width: "20%" }}
            onClick={handleLogin}
          >
            Login
          </Button> */}
                <Link
                  to="/login"
                  style={{
                    borderRadius: '6px',
                    textDecoration: 'none',
                    height: '36px',
                    width: '80px',
                    backgroundColor: 'green',
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                  }}
                >
                  <span
                    style={{ color: '#ffffff', textDecoration: 'none', display: 'flex', justifyContent: 'center', alignItems: 'center' }}
                  >
                    Login
                  </span>
                </Link>
              </Container>
            </Grid>
          </Grid>
        </div>
      )}
    </>
  );
};

export default Home;
