import React from 'react'
import { InputType } from "./InputType.ts"

interface CheckboxInputProps {
  type: InputType.CHECKBOX;
  label: string;
  checked: boolean;
  onChange: (value: boolean) => void;
}

interface RadioInputProps {
  type: InputType.RADIO;
  options: string[];
  selectedOption: string;
  onChange: (value: string) => void;
  name: string;
}

interface DropdownInputProps {
  type: InputType.DROPDOWN;
  label: string;
  options: string[];
  selectedOption: string;
  onChange: (value: string) => void;
}

interface DateInputProps {
  type: InputType.DATE;
  label: string;
  selectedOption: string;
  onChange: (value: string) => void;
}

type FormInputProps = CheckboxInputProps | RadioInputProps | DropdownInputProps | DateInputProps;

export const FormInput: React.FC<FormInputProps> = (props: FormInputProps) => {
  switch (props.type) {
    case InputType.CHECKBOX:
      return (
        <label>
          <input
            type="checkbox"
            checked={props.checked}
            onChange={(e) => props.onChange(e.target.checked)}
          />
          {props.label}
        </label>
      );

    case InputType.RADIO:
      return (
        <div>
          {props.options.map((option) => (
            <label key={option}>
              <input
                type="radio"
                name={props.name}
                value={option}
                checked={props.selectedOption === option}
                onChange={() => props.onChange(option)}
              />
              {option}
            </label>
          ))}
        </div>
      );

    case InputType.DROPDOWN:
      return (
        <label>
          {props.label}
          <select value={props.selectedOption} onChange={(e) => props.onChange(e.target.value)}>
            <option value="">Select...</option>
            {props.options.map((option) => (
              <option key={option} value={option}>
                {option}
              </option>
            ))}
          </select>
        </label>
      );

    case InputType.DATE:
      return (
        <label>
          {props.label}
          <input
            type="date"
            value={props.selectedOption}
            onChange={(e) => props.onChange(e.target.value)}
          />
        </label>
      );

    default:
      return null;
  }
};
