/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.ast;

import java.util.ArrayList;

import org.eclipse.wst.jsdt.core.ast.IASTNode;
import org.eclipse.wst.jsdt.core.ast.IExpression;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowContext;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowInfo;
import org.eclipse.wst.jsdt.internal.compiler.impl.Constant;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BaseTypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Binding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ClassScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeIds;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeVariableBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.WildcardBinding;

public abstract class Expression extends Statement implements IExpression {

	public Constant constant;

	public int statementEnd = -1;

	//Some expression may not be used - from a java semantic point
	//of view only - as statements. Other may. In order to avoid the creation
	//of wrappers around expression in order to tune them as expression
	//Expression is a subclass of Statement. See the message isValidJavaStatement()

	public int implicitConversion;
	public TypeBinding resolvedType;

public static final boolean isConstantValueRepresentable(Constant constant, int constantTypeID, int targetTypeID) {
	//true if there is no loss of precision while casting.
	// constantTypeID == constant.typeID
	if (targetTypeID == constantTypeID || constantTypeID==T_any)
		return true;
	switch (targetTypeID) {
		case T_char :
			switch (constantTypeID) {
				case T_char :
					return true;
				case T_double :
					return constant.doubleValue() == constant.charValue();
				case T_float :
					return constant.floatValue() == constant.charValue();
				case T_int :
					return constant.intValue() == constant.charValue();
				case T_short :
					return constant.shortValue() == constant.charValue();
				case T_byte :
					return constant.byteValue() == constant.charValue();
				case T_long :
					return constant.longValue() == constant.charValue();
				default :
					return false;//boolean
			}

		case T_float :
			switch (constantTypeID) {
				case T_char :
					return constant.charValue() == constant.floatValue();
				case T_double :
					return constant.doubleValue() == constant.floatValue();
				case T_float :
					return true;
				case T_int :
					return constant.intValue() == constant.floatValue();
				case T_short :
					return constant.shortValue() == constant.floatValue();
				case T_byte :
					return constant.byteValue() == constant.floatValue();
				case T_long :
					return constant.longValue() == constant.floatValue();
				default :
					return false;//boolean
			}

		case T_double :
			switch (constantTypeID) {
				case T_char :
					return constant.charValue() == constant.doubleValue();
				case T_double :
					return true;
				case T_float :
					return constant.floatValue() == constant.doubleValue();
				case T_int :
					return constant.intValue() == constant.doubleValue();
				case T_short :
					return constant.shortValue() == constant.doubleValue();
				case T_byte :
					return constant.byteValue() == constant.doubleValue();
				case T_long :
					return constant.longValue() == constant.doubleValue();
				default :
					return false; //boolean
			}

		case T_byte :
			switch (constantTypeID) {
				case T_char :
					return constant.charValue() == constant.byteValue();
				case T_double :
					return constant.doubleValue() == constant.byteValue();
				case T_float :
					return constant.floatValue() == constant.byteValue();
				case T_int :
					return constant.intValue() == constant.byteValue();
				case T_short :
					return constant.shortValue() == constant.byteValue();
				case T_byte :
					return true;
				case T_long :
					return constant.longValue() == constant.byteValue();
				default :
					return false; //boolean
			}

		case T_short :
			switch (constantTypeID) {
				case T_char :
					return constant.charValue() == constant.shortValue();
				case T_double :
					return constant.doubleValue() == constant.shortValue();
				case T_float :
					return constant.floatValue() == constant.shortValue();
				case T_int :
					return constant.intValue() == constant.shortValue();
				case T_short :
					return true;
				case T_byte :
					return constant.byteValue() == constant.shortValue();
				case T_long :
					return constant.longValue() == constant.shortValue();
				default :
					return false; //boolean
			}

		case T_int :
			switch (constantTypeID) {
				case T_char :
					return constant.charValue() == constant.intValue();
				case T_double :
					return constant.doubleValue() == constant.intValue();
				case T_float :
					return constant.floatValue() == constant.intValue();
				case T_int :
					return true;
				case T_short :
					return constant.shortValue() == constant.intValue();
				case T_byte :
					return constant.byteValue() == constant.intValue();
				case T_long :
					return constant.longValue() == constant.intValue();
				default :
					return false; //boolean
			}

		case T_long :
			switch (constantTypeID) {
				case T_char :
					return constant.charValue() == constant.longValue();
				case T_double :
					return constant.doubleValue() == constant.longValue();
				case T_float :
					return constant.floatValue() == constant.longValue();
				case T_int :
					return constant.intValue() == constant.longValue();
				case T_short :
					return constant.shortValue() == constant.longValue();
				case T_byte :
					return constant.byteValue() == constant.longValue();
				case T_long :
					return true;
				default :
					return false; //boolean
			}

		default :
			return false; //boolean
	}
}

public Expression() {
	super();
}

public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
	return flowInfo;
}

/**
 * More sophisticated for of the flow analysis used for analyzing expressions, and be able to optimize out
 * portions of expressions where no actual value is required.
 *
 * @param currentScope
 * @param flowContext
 * @param flowInfo
 * @param valueRequired
 * @return The state of initialization after the analysis of the current expression
 */
public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, boolean valueRequired) {

	return analyseCode(currentScope, flowContext, flowInfo);
}

/**
 * Returns false if cast is not legal.
 */
public final boolean checkCastTypesCompatibility(Scope scope, TypeBinding castType, TypeBinding expressionType, Expression expression) {

	// see specifications 5.5
	// handle errors and process constant when needed

	// if either one of the type is null ==>
	// some error has been already reported some where ==>
	// we then do not report an obvious-cascade-error.

	if (castType == null || expressionType == null) return true;
	if (castType==expressionType || castType.id==expressionType.id)
		return true;

	// identity conversion cannot be performed upfront, due to side-effects
	// like constant propagation
	boolean use15specifics = scope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5;
	if (castType.isBaseType()) {
		if (expressionType.isBaseType()) {
			if (expressionType == castType) {
				if (expression != null) {
					this.constant = expression.constant; //use the same constant
				}
				tagAsUnnecessaryCast(scope, castType);
				return true;
			}
			boolean necessary = false;
			if (expressionType.isCompatibleWith(castType)
					|| (necessary = BaseTypeBinding.isNarrowing(castType.id, expressionType.id))) {
				if (expression != null) {
					expression.implicitConversion = (castType.id << 4) + expressionType.id;
					if (expression.constant != Constant.NotAConstant) {
						this.constant = expression.constant.castTo(expression.implicitConversion);
					}
				}
				if (!necessary) tagAsUnnecessaryCast(scope, castType);
				return true;

			}
		} else if (use15specifics
							&& scope.environment().computeBoxingType(expressionType).isCompatibleWith(castType)) { // unboxing - only widening match is allowed
			tagAsUnnecessaryCast(scope, castType);
			return true;
		}
		return false;
	} else if (use15specifics
						&& expressionType.isBaseType()
						&& scope.environment().computeBoxingType(expressionType).isCompatibleWith(castType)) { // boxing - only widening match is allowed
		tagAsUnnecessaryCast(scope, castType);
		return true;
	}

	switch(expressionType.kind()) {
		case Binding.BASE_TYPE :
			//-----------cast to something which is NOT a base type--------------------------
			if (expressionType == TypeBinding.NULL) {
				tagAsUnnecessaryCast(scope, castType);
				return true; //null is compatible with every thing
			}
			return false;

		case Binding.ARRAY_TYPE :
			if (castType == expressionType) {
				tagAsUnnecessaryCast(scope, castType);
				return true; // identity conversion
			}
			switch (castType.kind()) {
				case Binding.ARRAY_TYPE :
					// ( ARRAY ) ARRAY
					TypeBinding castElementType = ((ArrayBinding) castType).elementsType();
					TypeBinding exprElementType = ((ArrayBinding) expressionType).elementsType();
					if (exprElementType.isBaseType() || castElementType.isBaseType()) {
						if (castElementType == exprElementType) {
							tagAsNeedCheckCast();
							return true;
						}
						return false;
					}
					// recurse on array type elements
					return checkCastTypesCompatibility(scope, castElementType, exprElementType, expression);

				case Binding.TYPE_PARAMETER :
					// ( TYPE_PARAMETER ) ARRAY
					TypeBinding match = expressionType.findSuperTypeWithSameErasure(castType);
					if (match == null) {
						checkUnsafeCast(scope, castType, expressionType, null /*no match*/, true);
					}
					// recurse on the type variable upper bound
					return checkCastTypesCompatibility(scope, ((TypeVariableBinding)castType).upperBound(), expressionType, expression);

				default:
					// ( CLASS/INTERFACE ) ARRAY
					switch (castType.id) {
						case T_JavaLangCloneable :
						case T_JavaIoSerializable :
							tagAsNeedCheckCast();
							return true;
						case T_JavaLangObject :
							tagAsUnnecessaryCast(scope, castType);
							return true;
						default :
							return false;
					}
			}

		case Binding.TYPE_PARAMETER :
			TypeBinding match = expressionType.findSuperTypeWithSameErasure(castType);
			if (match != null) {
				return checkUnsafeCast(scope, castType, expressionType, match, false);
			}
			// recursively on the type variable upper bound
			return checkCastTypesCompatibility(scope, castType, ((TypeVariableBinding)expressionType).upperBound(), expression);

		case Binding.WILDCARD_TYPE : // intersection type
			match = expressionType.findSuperTypeWithSameErasure(castType);
			if (match != null) {
				return checkUnsafeCast(scope, castType, expressionType, match, false);
			}
			// recursively on the type variable upper bound
			return checkCastTypesCompatibility(scope, castType, ((WildcardBinding)expressionType).bound, expression);

		default:
			if (expressionType.isInterface()) {
				switch (castType.kind()) {
					case Binding.ARRAY_TYPE :
						// ( ARRAY ) INTERFACE
						switch (expressionType.id) {
							case T_JavaLangCloneable :
							case T_JavaIoSerializable :
								tagAsNeedCheckCast();
								return true;
							default :
								return false;
						}

					case Binding.TYPE_PARAMETER :
						// ( INTERFACE ) TYPE_PARAMETER
						match = expressionType.findSuperTypeWithSameErasure(castType);
						if (match == null) {
							checkUnsafeCast(scope, castType, expressionType, null /*no match*/, true);
						}
						// recurse on the type variable upper bound
						return checkCastTypesCompatibility(scope, ((TypeVariableBinding)castType).upperBound(), expressionType, expression);

					default :
						if (castType.isInterface()) {
							// ( INTERFACE ) INTERFACE
							ReferenceBinding interfaceType = (ReferenceBinding) expressionType;
							match = interfaceType.findSuperTypeWithSameErasure(castType);
							if (match != null) {
								return checkUnsafeCast(scope, castType, interfaceType, match, false);
							}
							tagAsNeedCheckCast();
							match = castType.findSuperTypeWithSameErasure(interfaceType);
							if (match != null) {
								return checkUnsafeCast(scope, castType, interfaceType, match, true);
							}
							if (use15specifics) {
								checkUnsafeCast(scope, castType, expressionType, null /*no match*/, true);
								// ensure there is no collision between both interfaces: i.e. I1 extends List<String>, I2 extends List<Object>
								if (interfaceType.hasIncompatibleSuperType((ReferenceBinding)castType))
									return false;
							} else {
								// pre1.5 semantics - no covariance allowed (even if 1.5 compliant, but 1.4 source)
								MethodBinding[] castTypeMethods = getAllInheritedMethods((ReferenceBinding) castType);
								MethodBinding[] expressionTypeMethods = getAllInheritedMethods((ReferenceBinding) expressionType);
								int exprMethodsLength = expressionTypeMethods.length;
								for (int i = 0, castMethodsLength = castTypeMethods.length; i < castMethodsLength; i++) {
									for (int j = 0; j < exprMethodsLength; j++) {
										if ((castTypeMethods[i].returnType != expressionTypeMethods[j].returnType)
												&& (CharOperation.equals(castTypeMethods[i].selector, expressionTypeMethods[j].selector))
												&& castTypeMethods[i].areParametersEqual(expressionTypeMethods[j])) {
											return false;

										}
									}
								}
							}
							return true;
						} else {
							// ( CLASS ) INTERFACE
							if (castType.id == TypeIds.T_JavaLangObject) { // no runtime error
								tagAsUnnecessaryCast(scope, castType);
								return true;
							}
							// can only be a downcast
							tagAsNeedCheckCast();
							match = castType.findSuperTypeWithSameErasure(expressionType);
							if (match != null) {
								return checkUnsafeCast(scope, castType, expressionType, match, true);
							}
							if (((ReferenceBinding) castType).isFinal()) {
								// no subclass for castType, thus compile-time check is invalid
								return false;
							}
							if (use15specifics) {
								checkUnsafeCast(scope, castType, expressionType, null /*no match*/, true);
								// ensure there is no collision between both interfaces: i.e. I1 extends List<String>, I2 extends List<Object>
								if (((ReferenceBinding)castType).hasIncompatibleSuperType((ReferenceBinding) expressionType)) {
									return false;
								}
							}
							return true;
						}
				}
			} else {
				switch (castType.kind()) {
					case Binding.ARRAY_TYPE :
						// ( ARRAY ) CLASS
						if (expressionType.id == TypeIds.T_JavaLangObject) { // potential runtime error
							if (use15specifics) checkUnsafeCast(scope, castType, expressionType, expressionType, true);
							tagAsNeedCheckCast();
							return true;
						}
						return false;

					case Binding.TYPE_PARAMETER :
						// ( TYPE_PARAMETER ) CLASS
						match = expressionType.findSuperTypeWithSameErasure(castType);
						if (match == null) {
							checkUnsafeCast(scope, castType, expressionType, match, true);
						}
						// recurse on the type variable upper bound
						return checkCastTypesCompatibility(scope, ((TypeVariableBinding)castType).upperBound(), expressionType, expression);

					default :
						if (castType.isInterface()) {
							// ( INTERFACE ) CLASS
							ReferenceBinding refExprType = (ReferenceBinding) expressionType;
							match = refExprType.findSuperTypeWithSameErasure(castType);
							if (match != null) {
								return checkUnsafeCast(scope, castType, expressionType, match, false);
							}
							// unless final a subclass may implement the interface ==> no check at compile time
							if (refExprType.isFinal()) {
								return false;
							}
							tagAsNeedCheckCast();
							match = castType.findSuperTypeWithSameErasure(expressionType);
							if (match != null) {
								return checkUnsafeCast(scope, castType, expressionType, match, true);
							}
							if (use15specifics) {
								checkUnsafeCast(scope, castType, expressionType, null /*no match*/, true);
								// ensure there is no collision between both interfaces: i.e. I1 extends List<String>, I2 extends List<Object>
								if (refExprType.hasIncompatibleSuperType((ReferenceBinding) castType))
									return false;
							}
							return true;
						} else {
							// ( CLASS ) CLASS
							match = expressionType.findSuperTypeWithSameErasure(castType);
							if (match != null) {
								if (expression != null && castType.id == TypeIds.T_JavaLangString) this.constant = expression.constant; // (String) cst is still a constant
								return checkUnsafeCast(scope, castType, expressionType, match, false);
							}
							match = castType.findSuperTypeWithSameErasure(expressionType);
							if (match != null) {
								tagAsNeedCheckCast();
								return checkUnsafeCast(scope, castType, expressionType, match, true);
							}
							return false;
						}
				}
			}
	}
}

/**
 * Check the local variable of this expression, if any, against potential NPEs
 * given a flow context and an upstream flow info. If so, report the risk to
 * the context. Marks the local as checked, which affects the flow info.
 * @param scope the scope of the analysis
 * @param flowContext the current flow context
 * @param flowInfo the upstream flow info; caveat: may get modified
 */
public void checkNPE(BlockScope scope, FlowContext flowContext,
		FlowInfo flowInfo) {
	LocalVariableBinding local = this.localVariableBinding();
	if (local != null /*&&
			(local.type.tagBits & TagBits.IsBaseType) == 0*/) {
		if ((this.bits & ASTNode.IsNonNull) == 0) {
			flowContext.recordUsingNullReference(scope, local, this,
					FlowContext.MAY_NULL, flowInfo);
		}
		flowInfo.markAsComparedEqualToNonNull(local);
			// from thereon it is set
		if (flowContext.initsOnFinally != null) {
			flowContext.initsOnFinally.markAsComparedEqualToNonNull(local);
		}
	}
}

public boolean checkUnsafeCast(Scope scope, TypeBinding castType, TypeBinding expressionType, TypeBinding match, boolean isNarrowing) {
		if (match == castType) {
			if (!isNarrowing) tagAsUnnecessaryCast(scope, castType);
			return true;
		}
		if (match != null && (
				castType.isBoundParameterizedType()
				|| 	expressionType.isBoundParameterizedType())) {

			if (match.isProvablyDistinctFrom(isNarrowing ? expressionType : castType, 0)) {
				return false;
			}
		}
		if (!isNarrowing) tagAsUnnecessaryCast(scope, castType);
		return true;
	}
	/**
	 * Base types need that the widening is explicitly done by the compiler using some bytecode like i2f.
	 * Also check unsafe type operations.
	 */
	public void computeConversion(Scope scope, TypeBinding runtimeType, TypeBinding compileTimeType) {

//		if (runtimeType == null || compileTimeType == null)
//			return;
//		if (this.implicitConversion != 0) return; // already set independantly
//
//		// it is possible for a Byte to be unboxed to a byte & then converted to an int
//		// but it is not possible for a byte to become Byte & then assigned to an Integer,
//		// or to become an int before boxed into an Integer
//		if (runtimeType != TypeBinding.NULL && runtimeType.isBaseType()) {
//			if (!compileTimeType.isBaseType()) {
//				TypeBinding unboxedType = scope.environment().computeBoxingType(compileTimeType);
//				this.implicitConversion = TypeIds.UNBOXING;
//				scope.problemReporter().autoboxing(this, compileTimeType, runtimeType);
//				compileTimeType = unboxedType;
//			}
//		} else if (compileTimeType != TypeBinding.NULL && compileTimeType.isBaseType()) {
//			TypeBinding boxedType = scope.environment().computeBoxingType(runtimeType);
//			if (boxedType == runtimeType) // Object o = 12;
//				boxedType = compileTimeType;
//			this.implicitConversion = TypeIds.BOXING | (boxedType.id << 4) + compileTimeType.id;
//			scope.problemReporter().autoboxing(this, compileTimeType, scope.environment().computeBoxingType(boxedType));
//			return;
//		} else if (this.constant != Constant.NotAConstant && this.constant.typeID() != TypeIds.T_JavaLangString) {
//			this.implicitConversion = TypeIds.BOXING;
//			return;
//		}
//		int compileTimeTypeID, runtimeTypeID;
//		if ((compileTimeTypeID = compileTimeType.id) == TypeIds.NoId) { // e.g. ? extends String  ==> String (103227)
//			compileTimeTypeID = compileTimeType.erasure().id == TypeIds.T_JavaLangString ? TypeIds.T_JavaLangString : TypeIds.T_JavaLangObject;
//		}
//		switch (runtimeTypeID = runtimeType.id) {
//			case T_byte :
//			case T_short :
//			case T_char :
//				this.implicitConversion |= (TypeIds.T_int << 4) + compileTimeTypeID;
//				break;
//			case T_JavaLangString :
//			case T_float :
//			case T_boolean :
//			case T_double :
//			case T_int : //implicitConversion may result in i2i which will result in NO code gen
//			case T_long :
//				this.implicitConversion |= (runtimeTypeID << 4) + compileTimeTypeID;
//				break;
//			default : // regular object ref
////				if (compileTimeType.isRawType() && runtimeTimeType.isBoundParameterizedType()) {
////				    scope.problemReporter().unsafeRawExpression(this, compileTimeType, runtimeTimeType);
////				}
//		}
	}

	private MethodBinding[] getAllInheritedMethods(ReferenceBinding binding) {
		ArrayList collector = new ArrayList();
		getAllInheritedMethods0(binding, collector);
		return (MethodBinding[]) collector.toArray(new MethodBinding[collector.size()]);
	}

	private void getAllInheritedMethods0(ReferenceBinding binding, ArrayList collector) {
		if (!binding.isInterface()) return;
		MethodBinding[] methodBindings = binding.methods();
		for (int i = 0, max = methodBindings.length; i < max; i++) {
			collector.add(methodBindings[i]);
		}
		ReferenceBinding[] superInterfaces = binding.superInterfaces();
		for (int i = 0, max = superInterfaces.length; i < max; i++) {
			getAllInheritedMethods0(superInterfaces[i], collector);
		}
	}

	public boolean isCompactableOperation() {

		return false;
	}

	//Return true if the conversion is done AUTOMATICALLY by the vm
	//while the javaVM is an int based-machine, thus for example pushing
	//a byte onto the stack , will automatically create an int on the stack
	//(this request some work d be done by the VM on signed numbers)
	public boolean isConstantValueOfTypeAssignableToType(TypeBinding constantType, TypeBinding targetType) {

		if (this.constant == Constant.NotAConstant)
			return false;
		if (constantType == targetType)
			return true;
		if (constantType.id==targetType.id)
			return true;
		if (constantType.isBaseType() && targetType.isBaseType()) {
			//No free assignment conversion from anything but to integral ones.
			if ((constantType == TypeBinding.INT
				|| BaseTypeBinding.isWidening(TypeIds.T_int, constantType.id))
				&& (BaseTypeBinding.isNarrowing(targetType.id, TypeIds.T_int))) {
				//use current explicit conversion in order to get some new value to compare with current one
				return isConstantValueRepresentable(this.constant, constantType.id, targetType.id);
			}
		}
		return false;
	}

	public boolean isTypeReference() {
		return false;
	}

	/**
	 * Returns the local variable referenced by this node. Can be a direct reference (SingleNameReference)
	 * or thru a cast expression etc...
	 */
	public LocalVariableBinding localVariableBinding() {
		return null;
	}

/**
 * Mark this expression as being non null, per a specific tag in the
 * source code.
 */
// this is no more called for now, waiting for inter procedural null reference analysis
public void markAsNonNull() {
	this.bits |= ASTNode.IsNonNull;
}

	public int nullStatus(FlowInfo flowInfo) {

		if (/* (this.bits & IsNonNull) != 0 || */
				this.constant != null && this.constant != Constant.NotAConstant)
			return FlowInfo.NON_NULL; // constant expression cannot be null

		LocalVariableBinding local = localVariableBinding();
		if (local != null) {
			if (flowInfo.isDefinitelyNull(local))
				return FlowInfo.NULL;
			if (flowInfo.isDefinitelyNonNull(local))
				return FlowInfo.NON_NULL;
			return FlowInfo.UNKNOWN;
		}
		return FlowInfo.NON_NULL;
	}

	/**
	 * Constant usable for bytecode pattern optimizations, but cannot be inlined
	 * since it is not strictly equivalent to the definition of constant expressions.
	 * In particular, some side-effects may be required to occur (only the end value
	 * is known).
	 * @return Constant known to be of boolean type
	 */
	public Constant optimizedBooleanConstant() {
		return this.constant;
	}

	/**
	 * Returns the type of the expression after required implicit conversions. When expression type gets promoted
	 * or inserted a generic cast, the converted type will differ from the resolved type (surface side-effects from
	 * #computeConversion(...)).
	 * @return the type after implicit conversion
	 */
	public TypeBinding postConversionType(Scope scope) {
		TypeBinding convertedType = this.resolvedType;
		int runtimeType = (this.implicitConversion & TypeIds.IMPLICIT_CONVERSION_MASK) >> 4;
		switch (runtimeType) {
			case T_boolean :
				convertedType = TypeBinding.BOOLEAN;
				break;
			case T_byte :
				convertedType = TypeBinding.BYTE;
				break;
			case T_short :
				convertedType = TypeBinding.SHORT;
				break;
			case T_char :
				convertedType = TypeBinding.CHAR;
				break;
			case T_int :
				convertedType = TypeBinding.INT;
				break;
			case T_float :
				convertedType = TypeBinding.FLOAT;
				break;
			case T_long :
				convertedType = TypeBinding.LONG;
				break;
			case T_double :
				convertedType = TypeBinding.DOUBLE;
				break;
			default :
		}
		if ((this.implicitConversion & TypeIds.BOXING) != 0) {
			convertedType = scope.environment().computeBoxingType(convertedType);
		}
		return convertedType;
	}

	public StringBuffer print(int indent, StringBuffer output) {
		printIndent(indent, output);
		return printExpression(indent, output);
	}

	public abstract StringBuffer printExpression(int indent, StringBuffer output);

	public StringBuffer printStatement(int indent, StringBuffer output) {
		return print(indent, output).append(";"); //$NON-NLS-1$
	}

	public void resolve(BlockScope scope) {
		// drops the returning expression's type whatever the type is.

		this.resolveType(scope);
		return;
	}

	/**
	 * Resolve the type of this expression in the context of a blockScope
	 *
	 * @param scope
	 * @return
	 * 	Return the actual type of this expression after resolution
	 */
	public TypeBinding resolveType(BlockScope scope) {
		// by default... subclasses should implement a better TB if required.
		return null;
	}


	public TypeBinding resolveType(BlockScope scope, boolean define, TypeBinding useType) {
		return resolveType(scope);
	}

	/**
	 * Resolve the type of this expression in the context of a classScope
	 *
	 * @param scope
	 * @return
	 * 	Return the actual type of this expression after resolution
	 */
	public TypeBinding resolveType(ClassScope scope) {
		// by default... subclasses should implement a better TB if required.
		return null;
	}


	public TypeBinding resolveTypeExpecting(
		BlockScope scope,
		TypeBinding [] expectedTypes) {

		this.setExpectedType(expectedTypes[0]); // needed in case of generic method invocation
		TypeBinding expressionType = this.resolveType(scope);
		if (expressionType == null) return null;

		for (int i = 0; i < expectedTypes.length; i++) {
			if (expressionType == expectedTypes[i]) return expressionType;

			if (expressionType.isCompatibleWith(expectedTypes[i])) {
//				if (scope.isBoxingCompatibleWith(expressionType, expectedType)) {
//					this.computeConversion(scope, expectedType, expressionType);
//				} else {
//				}
					return expressionType;
			}
		}
		scope.problemReporter().typeMismatchError(expressionType, expectedTypes[0], this);
		return null;
	}


	public TypeBinding resolveTypeExpecting(
		BlockScope scope,
		TypeBinding expectedType) {

		this.setExpectedType(expectedType); // needed in case of generic method invocation
		TypeBinding expressionType = this.resolveType(scope);
		if (expressionType == null) return null;
		if (expressionType == expectedType) return expressionType;

		if (!expressionType.isCompatibleWith(expectedType)) {
			if (scope.isBoxingCompatibleWith(expressionType, expectedType)) {
				this.computeConversion(scope, expectedType, expressionType);
			} else {
				if (expectedType!=TypeBinding.BOOLEAN || expressionType==TypeBinding.VOID)
				{
					scope.problemReporter().typeMismatchError(expressionType, expectedType, this);
					return null;
				}
			}
		}
		return expressionType;
	}

	/**
	 * Returns an object which can be used to identify identical JSR sequence targets
	 * (see TryStatement subroutine codegen)
	 * or <code>null</null> if not reusable
	 */
	public Object reusableJSRTarget() {
		if (this.constant != Constant.NotAConstant)
			return this.constant;
		return null;
	}

	/**
	 * Record the type expectation before this expression is typechecked.
	 * e.g. String s = foo();, foo() will be tagged as being expected of type String
	 * Used to trigger proper inference of generic method invocations.
	 *
	 * @param expectedType
	 * 	The type denoting an expectation in the context of an assignment conversion
	 */
	public void setExpectedType(TypeBinding expectedType) {
	    // do nothing by default
	}

	public void tagAsNeedCheckCast() {
	    // do nothing by default
	}

	/**
	 * Record the fact a cast expression got detected as being unnecessary.
	 *
	 * @param scope
	 * @param castType
	 */
	public void tagAsUnnecessaryCast(Scope scope, TypeBinding castType) {
	    // do nothing by default
	}

	public Expression toTypeReference() {
		//by default undefined

		//this method is meanly used by the parser in order to transform
		//an expression that is used as a type reference in a cast ....
		//--appreciate the fact that castExpression and ExpressionWithParenthesis
		//--starts with the same pattern.....

		return this;
	}

	/**
	 * Traverse an expression in the context of a blockScope
	 * @param visitor
	 * @param scope
	 */
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		// nothing to do
	}

	/**
	 * Traverse an expression in the context of a classScope
	 * @param visitor
	 * @param scope
	 */
	public void traverse(ASTVisitor visitor, ClassScope scope) {
		// nothing to do
	}

	public void traverse(ASTVisitor visitior, Scope scope)
	{
		if (scope instanceof BlockScope)
			traverse(visitior,(BlockScope)scope);
		else if (scope instanceof ClassScope)
			traverse(visitior,(ClassScope)scope);
		else if (scope instanceof CompilationUnitScope)
			traverse(visitior,(CompilationUnitScope)scope);
	}

	public boolean isPrototype()
	{
		return false;
	}

	// is completion or selection node
	public boolean isSpecialNode()
	{
		return false;
	}
	
	public Binding alternateBinding()
	{ return null;}
	
	public TypeBinding resolveForAllocation(BlockScope scope, ASTNode location)
	{
		switch (getASTType()) {
		case IASTNode.STRING_LITERAL:
		case IASTNode.CHAR_LITERAL:
		case IASTNode.ARRAY_REFERENCE:
			
			break;

		default:
			System.out.println("IMPLEMENT resolveForAllocation for "+this.getClass());
			break;
		}
		return this.resolveType(scope);
	}
	
	public int getASTType() {
		return IASTNode.EXPRESSION;
	
	}
}
